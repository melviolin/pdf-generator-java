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

    # Diccionarios para almacenar la estructura final
    busqueda = {}
    paginas = {}

    # Lista para almacenar coordenadas por palabra
    coords_list = []

    for page_number, page in enumerate(doc, start=1):
        # Guardamos las dimensiones de la página
        paginas[str(page_number)] = {
            "height": float(page.rect.height),
            "width": float(page.rect.width)
        }

        text_instances = page.search_for(word)
        for inst in text_instances:
            coords_list.append({
                "page": page_number,
                "x0": float(inst.x0),
                "x1": float(inst.x1),
                "y0": float(inst.y0),
                "y1": float(inst.y1)
            })

    # Asociamos la palabra a su lista de coordenadas
    busqueda[word] = coords_list

    # Estructura final
    resultado = {
        "busqueda": busqueda,
        "paginas": paginas
    }

    return jsonify(resultado)
