export default function StatusBadge({ ativo }) {
    return (
        <span className={`status-badge ${ativo ? 'active' : 'inactive'}`}>
            {ativo ? 'ATIVO' : 'INATIVO'}
        </span>
    )
}