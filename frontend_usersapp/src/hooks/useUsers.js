import { useReducer, useState } from "react";
import { usersReducer } from "../reducers/usersReducer";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

const initialUsers = [
    {
        id: 1,
        username: 'Hr',
        password: '12345',
        email: 'agsolano-2023a@corhuila.edu.co'
    },
]

const initialUseForm = {
    id: 0,
    username: '',
    password: '',
    email: ''
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

    // Hook de enrutamiento para navegar entre pantallas
    const navigate = useNavigate();

    const handlerAddUser = (user) => {

        dispatch({
            type: (user.id === 0) ? 'addUser' : 'updateUser',
            payload: user,
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
    };

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
        setUserSelected({ initialUseForm });
    }

    return {
        // datos y estado expuestos por el hook
        users,
        userSelected,
        initialUseForm,
        visibleForm,

        // métodos para manipular el estado de usuarios
        handlerAddUser,
        handlerRemoveUser,
        handlerUserSelectedForm,
        handlerOpenForm,
        handlerCloseForm
    }
}