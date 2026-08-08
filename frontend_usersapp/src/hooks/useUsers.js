import { useReducer, useState } from "react";
import { usersReducer } from "../reducers/usersReducer";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { findAll, remove, save, update } from "../services/userService";

const initialUsers = [];

const initialUseForm = {
    id: 0,
    username: '',
    password: '',
    email: '',
}

const initialErrors = {
    username: '',
    password: '',
    email: '',
}

export const useUsers = () => {

    // Estado de los usuarios: useReducer gestiona la lista de usuarios
    // 'users' contiene el array actual y 'dispatch' se usa para enviar acciones
    // al reducer (usersReducer) que actualizará el estado según el tipo de acción.
    const [users, dispatch] = useReducer(usersReducer, initialUsers);

    // Estado del formulario: usuario seleccionado para editar o inicializar el formulario
    const [userSelected, setUserSelected] = useState(initialUseForm);

    // Controla si el formulario de usuario está visible o no
    const [visibleForm, setvisibleForm] = useState(false);

    // Estado de errores del formulario: almacena mensajes de error para cada campo
    const [errors, setErrors] = useState(initialErrors);

    // Hook de enrutamiento para navegar entre pantallas
    const navigate = useNavigate();

    // Funcion para obtener todos los usuarios desde el backend 
    const getusers = async () => {
        const result = await findAll();
        console.log(result);
        dispatch({
            type: 'loadingUsers',
            payload: result.data,
        });
    }

    const handlerAddUser = async (user) => {
        console.log(user);
        let response;

        try {

            if (user.id === 0) {
                response = await save(user);
            } else {
                response = await update(user);
            }

            dispatch({
                type: (user.id === 0) ? 'addUser' : 'updateUser',
                payload: response.data,
            })

            Swal.fire(
                (user.id === 0) ?
                    "Usuario creado" :
                    "Usuario actualizado",
                (user.id === 0) ?
                    "El usuario ha sido creado con exito" :
                    "El usuario ha sido actualizado con exito",
                "success"
            );
            handlerCloseForm();
            navigate('/users');



        } catch (error) {
            if (error.response && error.response.status === 400) {
                setErrors(error.response.data);
            } else if (error.response && error.response.status === 500 ) {

                    //mysql> SHOW INDEX FROM users; 
                if (error.response.data?.message?.includes('UK_username')) {
                    setErrors({
                        username: 'El nombre de usuario ya existe',
                    });
                }
                    //mysql> SHOW INDEX FROM users; 
                if (error.response.data?.message?.includes('UK_email')) {
                    setErrors({
                        email: 'El Email de usuario ya existe',
                    });
                }
                
            } else {
                throw error;
            }
        };
    }

    const handlerRemoveUser = (id) => {

        Swal.fire({
            title: "Esta seguro?",
            text: "¡No podrás revertir esto!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Si, eliminar!"
        }).then((result) => {
            if (result.isConfirmed) {
                remove(id);
                dispatch({
                    type: 'removeUser',
                    payload: id,
                });
                Swal.fire({
                    title: "Usuario eliminado!",
                    text: "El usuario ha sido eliminado con exito.",
                    icon: "success"
                })
            };
        });
    }

    const handlerUserSelectedForm = (user) => {
        setvisibleForm(true);
        setUserSelected({ ...user });
    }

    const handlerOpenForm = () => {
        setvisibleForm(true);
    }
    const handlerCloseForm = () => {
        setvisibleForm(false);
        setUserSelected(initialUseForm);
        setErrors({});
    }

    return {
        // datos y estado expuestos por el hook
        users,
        userSelected,
        initialUseForm,
        visibleForm,
        errors,

        // métodos para manipular el estado de usuarios
        handlerAddUser,
        handlerRemoveUser,
        handlerUserSelectedForm,
        handlerOpenForm,
        handlerCloseForm,
        getusers,
    };
}