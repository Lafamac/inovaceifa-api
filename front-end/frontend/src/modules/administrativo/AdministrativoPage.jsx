import { useEffect, useState } from "react";
import { apiFetch } from "../../api/api";
import { useNavigate } from "react-router-dom";

export default function AdministrativoPage() {
  const [data, setData] = useState([]);
  const [tiposRateio, setTiposRateio] = useState([]);
  const navigate = useNavigate();

  const carregar = async () => {
    const res = await apiFetch("/administrativo");

    if (res.ok) {
      setData(res.data?.data?.content || []);
    }
  };

  const carregarTiposRateio = async () => {
    const res = await apiFetch("/admin/referencias/tipo-rateio");

    if (res.ok) {
      setTiposRateio(res.data?.data || []);
    }
  };

  const getDescricaoRateio = (id) => {
    const item = tiposRateio.find((t) => t.id === id);
    return item ? item.descricao : "-";
  };

  useEffect(() => {
    carregar();
    carregarTiposRateio();
  }, []);

  const excluir = async (id) => {
    await apiFetch(`/administrativo/${id}`, {
      method: "DELETE"
    });

    carregar();
  };

  return (
    <div>
      <h2>Administrativo</h2>

      <button
        onClick={() => navigate("/custos/administrativo/novo")}
        style={{ marginBottom: 15 }}
      >
        + Novo
      </button>

      <table style={{ width: "100%", borderSpacing: "0 8px" }}>
        <thead>
          <tr>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>Descrição</th>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>Mês</th>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>Tipo Rateio</th>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>Planejado</th>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>Realizado</th>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>R$/ha</th>
            <th style={{ textAlign: "left", padding: "8px 12px" }}>Ações</th>
          </tr>
        </thead>

        <tbody>
          {data.map((item) => (
            <tr key={item.id} style={{ background: "#fff" }}>
              <td style={{ padding: "10px 12px" }}>{item.descricao}</td>
              <td style={{ padding: "10px 12px" }}>{item.mesAno}</td>
              <td style={{ padding: "10px 12px" }}>{getDescricaoRateio(item.tipoRateioId)}</td>
              <td style={{ padding: "10px 12px" }}>R$ {item.valorTotalPlanejado}</td>
              <td style={{ padding: "10px 12px" }}>R$ {item.valorTotalRealizado}</td>
              <td style={{ padding: "10px 12px" }}>R$ {item.valorHaRealizado}</td>

              <td style={{ padding: "10px 12px", display: "flex", gap: 8 }}>
                <button
                  onClick={() =>
                    navigate(`/custos/administrativo/${item.id}`)
                  }
                >
                  Editar
                </button>

                <button onClick={() => excluir(item.id)}>
                  Excluir
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}