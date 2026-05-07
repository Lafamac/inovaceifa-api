export default function CrudCard({
    title,
    subtitle,
    description,
    ativo,
    onClick,
    actions,
    disabled
}) {
    return (
        <div
            className={`crud-card ${disabled ? 'disabled' : ''}`}
            onClick={!disabled ? onClick : undefined}
        >
            <div
                className="crud-card-content"
                title={!disabled ? "Clique para visualizar detalhes" : ""}
            >
                <strong>{title}</strong>

                {subtitle && (
                    <div className="crud-subtitle">
                        {subtitle}
                    </div>
                )}

                {description && (
                    <div className="crud-description">
                        {description}
                    </div>
                )}
            </div>

            <div
                className="crud-card-actions"
                onClick={e => e.stopPropagation()}
            >
                {actions}
            </div>
        </div>
    )
}