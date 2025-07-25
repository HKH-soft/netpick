import { createContext,useContext,useEffect,useState } from "react";
import { login as performLogin ,saveCustomer as performRegister} from "../../services/client.js";
import {jwtDecode } from "jwt-decode"

const AuthContext = createContext({})

const AuthProvider = ({ children }) => {
    const [customer, setCustomer] = useState(null)

    useEffect(() => {
        let token = localStorage.getItem("access_token")
        if(token){
            token = jwtDecode(token)
            setCustomer({
                username: token.sub,
                roles: token.scopes
            })
        }
    },[])

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"]
                localStorage.setItem("access_token",jwtToken)
                const decodedToken = jwtDecode(jwtToken)
                setCustomer({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes
                })
                resolve(res); 
            }).catch(err => {
                reject(err);
            })
        })
    }

    const logout = async () => {
        localStorage.removeItem("access_token")
        setCustomer(null)
    }
    
    const isUserAuthenticated = () => {
        const token = localStorage.getItem("access_token")
        if(!token){
            return false
        }
        const {exp:expration} = jwtDecode(token)
        if(Date.now() > expration * 1000){
            logout()
            return false
        }
        return true
    }

    const register = async (customer) =>{
            return new Promise((resolve, reject) => {
            performRegister(customer).then(res => {
                const jwtToken = res.headers["authorization"]
                localStorage.setItem("access_token",jwtToken)
                const decodedToken = jwtDecode(jwtToken)
                setCustomer({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes
                })
                resolve(res); 
            }).catch(err => {
                reject(err);
            })
            })
    }
    
    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logout,
            isUserAuthenticated,
            register
        }} >
            {children }
        </AuthContext.Provider>
    )
}
export const useAuth = () => useContext(AuthContext) 
export default AuthProvider
