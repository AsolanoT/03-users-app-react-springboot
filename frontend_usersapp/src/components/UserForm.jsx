import { useContext, useEffect, useState } from "react"
import Swal from "sweetalert2";
import { UserContext } from "../context/UserContext";

export const UsersForm = ({ userSelected, handlerCloseForm }) => {

    const { initialUseForm, handlerAddUser } = useContext(UserContext);

    const [userForm, setUserForm] = useState(initialUseForm);

    const { id, username, password, email } = userForm;

    useEffect(() => {
        setUserForm({
            ...userSelected,
            password: '',
        });
    }, [userSelected]);

    const onInputChange = ({ target }) => {
        // console.log(target.value)
        const { name, value } = target;
        setUserForm({
            ...userForm,
            [name]: value,
        })
    }

    const onSubmit = (event) => {
        event.preventDefault();

        if (!username || (!password && id === 0) || !email) {
            Swal.fire({
                title: "Error de validacion",
                text: "Debe completar los datos del formulario",
                icon: "error"
            });
            return;
        }
        if (!email.includes('@')) {
            Swal.fire({
                title: "Error de Email",
                text: "El email no es valido, falta @",
                icon: "error"
            });
            return;
        }

        // Guardar el user form en el listado de usuarios
        handlerAddUser(userForm);
        setUserForm(initialUseForm);
    }

    const onCloseForm = () => {
        handlerCloseForm();
        setUserForm(initialUseForm);
    }
    return (
        <form onSubmit={onSubmit}>
            <input
                className="form-control my-3 w-75"
                placeholder="Username"
                name="username"
                value={username}
                onChange={onInputChange} />

            {id > 0 ||
                <input
                    className="form-control my-3 w-75"
                    placeholder="Password"
                    type="password"
                    name="password"
                    value={password}
                    onChange={onInputChange} />
            }

            <input
                className="form-control my-3 w-75"
                placeholder="Email"
                name="email"
                value={email}
                onChange={onInputChange} />

            <input
                type="hidden"
                name="id"
                value={id} />

            <button
                className="btn btn-primary"
                type="submit">
                {id > 0 ? 'Actualizar' : 'Crear'}
            </button>

            {!handlerCloseForm || <button
                className="btn btn-danger mx-2"
                type="button"
                onClick={() => onCloseForm()}>
                Cerrar
            </button>}





        </form>
    )
}
