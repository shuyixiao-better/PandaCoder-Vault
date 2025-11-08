import { Navigate } from 'react-router-dom'
import { auth } from '../utils/auth.js'

const PrivateRoute = ({ children }) => {
  return auth.isAuthenticated() ? children : <Navigate to="/login" replace />
}

export default PrivateRoute

