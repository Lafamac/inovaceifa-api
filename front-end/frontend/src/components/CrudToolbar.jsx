export default function CrudToolbar({
    busca,
    setBusca,
    onNovo,
    labelNovo,
    mostrarInativos,
    setMostrarInativos,
    labelToggle = 'Mostrar inativos' // 🔥 NOVO (opcional)
}) {
    return (
        <div className="actions">
            <input
                type="text"
                className="search-bar"
                placeholder="🔍 Buscar..."
                value={busca}
                onChange={e => setBusca(e.target.value)}
            />

            {onNovo && (
                <button className="add-btn" onClick={onNovo}>
                    + {labelNovo}
                </button>
            )}

            {setMostrarInativos && (
                <label className="toggle-inativos">
                    <input
                        type="checkbox"
                        checked={mostrarInativos}
                        onChange={() =>
                            setMostrarInativos(!mostrarInativos)
                        }
                    />
                    {labelToggle}
                </label>
            )}
        </div>
    )
}