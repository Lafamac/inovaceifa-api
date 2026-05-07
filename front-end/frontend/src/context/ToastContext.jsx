import { createContext, useContext, useState, useCallback } from 'react'

const ToastContext = createContext()

let toastId = 0

export function ToastProvider({ children }) {
    const [toasts, setToasts] = useState([])

    const removeToast = useCallback((id) => {
        setToasts(prev => prev.filter(t => t.id !== id))
    }, [])

    const showToast = useCallback((message, type = 'success', duration = 3000) => {
        const id = ++toastId

        const novoToast = { id, message, type }

        setToasts(prev => [...prev, novoToast])

        setTimeout(() => {
            removeToast(id)
        }, duration)

    }, [removeToast])

    return (
        <ToastContext.Provider value={{ showToast }}>
            {children}

            <div className="toast-container">
                {toasts.map(t => (
                    <div key={t.id} className={`toast ${t.type}`}>
                        {t.message}
                    </div>
                ))}
            </div>
        </ToastContext.Provider>
    )
}

// eslint-disable-next-line react-refresh/only-export-components
export function useToast() {
    return useContext(ToastContext)
}