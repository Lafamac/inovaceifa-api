import { useEffect, useState } from 'react'
import Select from 'react-select'
import { listarReferencias } from '../api/api'

export default function SelectReferencia({ tipo, value, onChange, label }) {

  const [options, setOptions] = useState([])

  useEffect(() => {
    async function carregar() {
      const res = await listarReferencias(tipo)

      const dados =
        res.data?.data?.content ||
        res.data?.data ||
        []

      const formatado = (Array.isArray(dados) ? dados : []).map(item => ({
        value: item.id,
        label: item.descricao
      }))

      setOptions(formatado)
    }

    carregar()
  }, [tipo])

  const selecionado = options.find(o => o.value === value) || null

  return (
    <div style={{ display: 'flex', flexDirection: 'column' }}>

      {label && (
        <label style={{ marginBottom: 4, fontWeight: 500 }}>
          {label}
        </label>
      )}

      <Select
        options={options}
        value={selecionado}
        onChange={(selected) => onChange(selected?.value || '')}
        placeholder="Selecione..."
        isClearable
      />

    </div>
  )
}