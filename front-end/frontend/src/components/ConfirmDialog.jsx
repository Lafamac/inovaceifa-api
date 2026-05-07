export default function ConfirmDialog({
    open,
    title,
    message,
    onConfirm,
    onCancel
}) {
    if (!open) return null

    return (
        <div className="dialog-overlay">
            <div className="dialog-box">
                <h3>{title}</h3>
                <p>{message}</p>

                <div className="dialog-actions">
                    <button className="btn-secondary" onClick={onCancel}>
                        Cancelar
                    </button>

                    <button className="btn-danger" onClick={onConfirm}>
                        Confirmar
                    </button>
                </div>
            </div>
        </div>
    )
}