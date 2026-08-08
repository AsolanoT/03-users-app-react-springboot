import { createContext } from "react";

// createContext crea un contexto global para compartir datos entre componentes
// sin necesidad de pasar props manualmente a través de cada nivel.
// Aquí se exporta el contexto del usuario para poder usarlo en otros componentes.
export const UserContext = createContext();