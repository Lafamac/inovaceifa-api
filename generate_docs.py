import json
import urllib.request
import sys

def main():
    try:
        req = urllib.request.Request("http://localhost:8080/v3/api-docs")
        with urllib.request.urlopen(req) as response:
            data = json.loads(response.read().decode())
    except Exception as e:
        print(f"Error fetching API docs: {e}")
        sys.exit(1)

    paths = data.get("paths", {})
    components = data.get("components", {}).get("schemas", {})

    with open("API_DOCUMENTATION.md", "w", encoding="utf-8") as f:
        f.write("# Inova Ceifa - Documentação da API Backend\n\n")
        f.write("> **Documentação gerada automaticamente para compartilhamento com a equipe.**\n")
        f.write("> URL Base Padrão: `http://localhost:8080` (em dev) ou URL de Produção.\n\n")
        
        # Agrupar por tags (Controllers)
        tags_map = {}
        for path, methods in paths.items():
            for method, details in methods.items():
                tags = details.get("tags", ["Outros"])
                tag = tags[0]
                if tag not in tags_map:
                    tags_map[tag] = []
                tags_map[tag].append({
                    "path": path,
                    "method": method.upper(),
                    "summary": details.get("summary", ""),
                    "details": details
                })
                
        for tag, endpoints in sorted(tags_map.items()):
            f.write(f"## {tag}\n\n")
            for ep in endpoints:
                f.write(f"### `[{ep['method']}] {ep['path']}`\n")
                if ep['summary']:
                    f.write(f"**Descrição:** {ep['summary']}\n\n")
                
                # Parameters (Query/Path)
                params = ep['details'].get("parameters", [])
                if params:
                    f.write("**Parâmetros na URL:**\n")
                    f.write("| Nome | Tipo | Onde | Obrigatório |\n")
                    f.write("|---|---|---|---|\n")
                    for p in params:
                        p_name = p.get("name", "")
                        p_in = p.get("in", "")
                        p_req = "Sim" if p.get("required") else "Não"
                        p_type = p.get("schema", {}).get("type", "string")
                        f.write(f"| `{p_name}` | `{p_type}` | {p_in} | {p_req} |\n")
                    f.write("\n")

                # Request Body
                req_body = ep['details'].get("requestBody")
                if req_body:
                    content = req_body.get("content", {})
                    if "application/json" in content:
                        schema_ref = content["application/json"].get("schema", {}).get("$ref")
                        if schema_ref:
                            schema_name = schema_ref.split("/")[-1]
                            f.write(f"**Corpo da Requisição (JSON):** `Modelo {schema_name}`\n\n")
                            
                            schema = components.get(schema_name, {})
                            props = schema.get("properties", {})
                            if props:
                                f.write("| Campo | Tipo | Descrição |\n")
                                f.write("|---|---|---|\n")
                                for p_name, p_details in props.items():
                                    p_type = p_details.get("type")
                                    if not p_type and "$ref" in p_details:
                                        p_type = "Objeto (" + p_details["$ref"].split("/")[-1] + ")"
                                    elif p_type == "array" and "items" in p_details:
                                        item_ref = p_details["items"].get("$ref")
                                        if item_ref:
                                            p_type = "Array de " + item_ref.split("/")[-1]
                                        else:
                                            p_type = "Array de " + p_details["items"].get("type", "desconhecido")
                                    
                                    desc = p_details.get("description", "")
                                    f.write(f"| `{p_name}` | `{p_type}` | {desc} |\n")
                                f.write("\n")
                
                f.write("---\n\n")

if __name__ == "__main__":
    main()
