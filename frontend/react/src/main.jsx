import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import {createBrowserRouter, RouterProvider} from "react-router-dom"
import AuthProvider from './components/context/AuthContext.jsx'
import Login from "./components/my-components/Login.jsx"
import './index.css'
import ProtectedRoute from './components/Shared/ProtectedRoute.jsx'
import Register from './components/my-components/Register.jsx'

const router = createBrowserRouter([
  {
    path: "/",
    element:<Login/>
  },
  {
    path: "dashboard",
    element: <ProtectedRoute><App/></ProtectedRoute>
  },
  {
    path: "register",
    element:<Register/>
  }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={router}/>
    </AuthProvider>
  </StrictMode>,
)
