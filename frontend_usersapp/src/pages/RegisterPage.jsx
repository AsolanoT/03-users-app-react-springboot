import { useContext, useEffect, useState } from "react"
import { UsersForm } from "../components/UserForm"
import { useParams } from "react-router-dom";
import { UserContext } from "../context/UserContext";

export const RegisterPage = () => {

    const { users = [], initialUseForm } = useContext(UserContext);

    const [userSelected, setUserSelected] = useState(initialUseForm);

    const { id } = useParams();

    useEffect(() => {
        if (id) {
            const user = users.find(u => u.id == id) || initialUseForm;
            setUserSelected(user);
        }
    }, [id]);

    return (
        <div className=" container my-4">
            <h4>{userSelected.id > 0 ? 'Editar Usuario' : 'Registro de Usuarios'}</h4>

            <div className="row">
                <UsersForm
                    userSelected={userSelected} />
            </div>
        </div>
    )
}
