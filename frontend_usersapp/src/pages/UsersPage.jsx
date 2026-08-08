import { useContext, useEffect } from "react";
import { UserModalForm } from "../components/UserModalForm";
import { UsersList } from "../components/UsersList"
import { UserContext } from "../context/UserContext";

export const UsersPage = () => {

    const {
        users,
        visibleForm,
        handlerOpenForm,
        getusers,
    } = useContext(UserContext);

    useEffect(() => {
        getusers();
    }, []);

    return (
        <>
            {!visibleForm ||
                <UserModalForm />
            }
            <div className="container my-4">
                <h2>Users App</h2>

                <div className="row">
                    {/* {!visibleForm ||
                    <div className="col">
                        <UsersForm
                            initialUseForm={initialUseForm}
                            userSelected={userSelected}
                            handlerAddUser={handlerAddUser} 
                            handlerCloseForm={handlerCloseForm}/>
                    </div>
                } */}

                    {/* Se muestra el botón para abrir el formulario cuando el formulario modal no está visible,
                        y se renderiza la lista de usuarios o un mensaje de alerta si no hay usuarios. */}
                    <div className="col">
                        {visibleForm ||
                            <button
                                className="btn btn-primary my-2"
                                onClick={handlerOpenForm}>
                                Nuevo usuario
                            </button>
                        }
                        {users.length === 0
                            ? <div className="alert alert-warning">No hay usuarios en el sistema</div>
                            : <UsersList />}
                    </div>

                </div>

            </div>
        </>
    )
}
