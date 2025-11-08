import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Dashboard from './pages/Dashboard.jsx'
import WeeklyReports from './pages/WeeklyReports.jsx'
import ReportDetail from './pages/ReportDetail.jsx'
import PrivateRoute from './components/PrivateRoute.jsx'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/*"
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      >
        <Route index element={<Navigate to="/reports" replace />} />
        <Route path="reports" element={<WeeklyReports />} />
        <Route path="reports/:id" element={<ReportDetail />} />
      </Route>
    </Routes>
  )
}

export default App

