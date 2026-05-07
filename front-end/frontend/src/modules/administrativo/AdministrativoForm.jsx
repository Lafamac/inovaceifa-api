import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { apiFetch } from "../../api/api";

import PageLayout from "../../components/PageLayout";
import Alert from "../../components/Alert";
import FormInput from "../../components/FormInput";
import SelectReferencia from "../../components/SelectReferencia";

export default function AdministrativoForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [erro, setErro] = useState("");

  const [form, setForm] = useState({
    descricao: "",
    mesAno: "",
    valorTotalPlanejado: "",
    valorTotalRealizado: "",
    tipoRateioId: "",
    contaGerencialId: "",
    despesaEducampoId: ""
  });

  useEffect(() => {
    if (id) {
      apiFetch(`/administrativo/${id}`).then(res => {
        if (res.ok) {
          setForm(res.data.data);
        }
      });
    }
  }, [id]);

  const formatarMoeda = (valor) => {
    if (!valor) return "";

    let v = valor.replace(/\D/g, "");
    v = (Number(v) / 100).toFixed(2);

    return v
      .replace(".", ",")
      .replace(/\B(?=(\d{3})+(?!\d))/g, ".");
  };

  const limparMoeda = (valor) => {
    if (!valor) return 0;
    return Number(valor.replace(/\./g, "").replace(",", "."));
  };

  const validar = () => {
    if (!form.descricao) return "Informe a descrição";
    if (!form.mesAno) return "Informe o mês";
    if (!form.tipoRateioId) return "Selecione o tipo de rateio";
    if (!form.contaGerencialId) return "Selecione a conta gerencial";
    if (!form.despesaEducampoId) return "Selecione a despesa";
    return "";
  };

  const salvar = async () => {
    const erroValidacao = validar();
    if (erroValidacao) {
      setErro(erroValidacao);
      return;
    }

    const payload = {
      ...form,
      tipoRateioId: Number(form.tipoRateioId),
      contaGerencialId: Number(form.contaGerencialId),
      despesaEducampoId: Number(form.despesaEducampoId),
      valorTotalPlanejado: limparMoeda(form.valorTotalPlanejado),
      valorTotalRealizado: limparMoeda(form.valorTotalRealizado)
    };

    if (id) {
      await apiFetch(`/administrativo/${id}`, {
        method: "PUT",
        body: JSON.stringify(payload)
      });
    } else {
      await apiFetch("/administrativo", {
        method: "POST",
        body: JSON.stringify(payload)
      });
    }

    navigate("/custos/administrativo");
  };

  return (
    <PageLayout
      title={id ? "Editar Administrativo" : "Novo Administrativo"}
      showBack
      backTo="/custos/administrativo"
    >

      {erro && <Alert type="error" message={erro} />}

      <div style={{
        display: "grid",
        gridTemplateColumns: "1fr 1fr",
        gap: 16,
        maxWidth: 800
      }}>

        <FormInput
          label="Descrição"
          value={form.descricao}
          onChange={(e) =>
            setForm({ ...form, descricao: e.target.value })
          }
        />

        <FormInput
          label="Mês"
          type="month"
          value={form.mesAno}
          onChange={(e) =>
            setForm({ ...form, mesAno: e.target.value })
          }
        />

        <FormInput
          label="Valor Planejado"
          value={form.valorTotalPlanejado}
          onChange={(e) =>
            setForm({
              ...form,
              valorTotalPlanejado: formatarMoeda(e.target.value)
            })
          }
        />

        <FormInput
          label="Valor Realizado"
          value={form.valorTotalRealizado}
          onChange={(e) =>
            setForm({
              ...form,
              valorTotalRealizado: formatarMoeda(e.target.value)
            })
          }
        />

        {/* 🔥 CORRETO */}
        <SelectReferencia
          label="Tipo de Rateio"
          tipo="tipo-rateio"
          value={form.tipoRateioId}
          onChange={(value) =>
            setForm({ ...form, tipoRateioId: value })
          }
        />

        <SelectReferencia
          label="Conta Gerencial"
          tipo="conta-gerencial"
          value={form.contaGerencialId}
          onChange={(value) =>
            setForm({ ...form, contaGerencialId: value })
          }
        />

        <SelectReferencia
          label="Despesa"
          tipo="despesa"
          value={form.despesaEducampoId}
          onChange={(value) =>
            setForm({ ...form, despesaEducampoId: value })
          }
        />

      </div>

      <div style={{ marginTop: 20 }}>
        <button onClick={salvar} className="add-btn">
          Salvar
        </button>
      </div>

    </PageLayout>
  );
}