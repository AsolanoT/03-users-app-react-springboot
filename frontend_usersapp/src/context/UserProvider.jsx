import { useUsers } from "../hooks/useUsers";
import { UserContext } from "./UserContext"

export const UserProvider = ({ children }) => {

    const {
        users,
        userSelected,
        initialUseForm,
        visibleForm,

        handlerAddUser,
        handlerRemoveUser,
        handlerUserSelectedForm,
        handlerOpenForm,
        handlerCloseForm,
        getusers
    } = useUsers();

    return (
        <UserContext.Provider value={
            {
                users,
                userSelected,
                initialUseForm,
                visibleForm,

                handlerAddUser,
                handlerRemoveUser,
                handlerUserSelectedForm,
                handlerOpenForm,
                handlerCloseForm,
                getusers
            }
        } >
            {children}
        </UserContext.Provider>
    )
}