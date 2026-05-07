import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import '../styles/pages.css'

export default function PageLayout({
    title,
    showBack = false,
    backTo = null,
    actions = null,
    children
}) {
    const navigate = useNavigate()

    function handleBack() {
        if (backTo) {
            navigate(backTo)
            return
        }
        navigate(-1)
    }

    return (
        <motion.div
            className="page-container"
            initial={{ opacity: 0, y: 8 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.25, ease: 'easeOut' }}
        >
            <div className="page-header">
                <div className="header-actions">
                    {showBack && (
                        <button
                            className="back-button"
                            onClick={handleBack}
                        >
                            ← Voltar
                        </button>
                    )}
                    {actions}
                </div>

                <h2>{title}</h2>
            </div>

            {children}
        </motion.div>
    )
}