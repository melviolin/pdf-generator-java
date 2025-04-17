from flask import Flask, request, jsonify
import fitz  # PyMuPDF
import io

app = Flask(__name__)

@app.route('/find-coords', methods=['POST'])
def find_coords():
    if 'file' not in request.files or 'word' not in request.form:
        return jsonify({"error": "Missing file or word"}), 400

    file = request.files['file']
    word = request.form['word']
    pdf_data = file.read()

    doc = fitz.open(stream=pdf_data, filetype="pdf")

    busqueda = {}
    paginas = {}
    coords_list = []

    for page_number, page in enumerate(doc, start=1):
        text_instances = page.search_for(word)
        if not text_instances:
            continue  # Saltar páginas sin resultados

        # Agregamos la dimensión solo si hubo coincidencias
        paginas[str(page_number)] = {
            "height": float(page.rect.height),
            "width": float(page.rect.width)
        }

        for inst in text_instances:
            coords_list.append({
                "page": page_number,
                "x0": float(inst.x0),
                "x1": float(inst.x1),
                "y0": float(inst.y0),
                "y1": float(inst.y1)
            })

    busqueda[word] = coords_list

    resultado = {
        "busqueda": busqueda,
        "paginas": paginas
    }

    return jsonify(resultado)
