export const findAll = () => {
    try {
        const responde = await axios.get('http://localhost:8080/api/users');
        return response;
    } catch (error) {
        console.log(error);
    }
    return null;
}