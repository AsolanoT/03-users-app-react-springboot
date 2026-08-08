export const loginReducer = ( state = {}, action ) => {

    // Switch que evalúa el tipo de acción
    switch (action.type) {
        // Case 'login': cuando el usuario inicia sesión
        case 'login':

        return {
            isAuth: true,  // Marca al usuario como autenticado
            user: action.payload,  // Guarda los datos del usuario
        };
        // Case 'logout': cuando el usuario cierra sesión
        case 'logout':
            return {
                isAuth: false,  // Marca al usuario como no autenticado
            };
        // Default: si la acción no coincide con ningún case, retorna el estado actual
        default:
            return state;
    }
}