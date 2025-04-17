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
    results = []

    for page_number, page in enumerate(doc, start=1):
        text_instances = page.search_for(word)
        for inst in text_instances:
            results.append({
                "page": page_number,
                "x": inst.x0,
                "y": inst.y0,
                "width": inst.width,
                "height": inst.height
            })

    return jsonify({"matches": results})
