package com.objects;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.objects.textures.*;
import com.shaders.*;
import com.utils.math.*;

public class Model extends Object {

	public Model() {

	}

	public Model(String name) {
		this.name = name;
		startThread();
	}

	protected void thread() {
		loadModel();
		subcomp.add(material);
		mesh.computeTangents();
		mesh.loadBuffers();
	}

	public void load() {
		if (ready && !loaded) {
			mesh.loadVBO();
			loaded = true;
		}
		material.load();
	}

	public void loadModel() {
		try {
			File file = new File("data/models/" + name + ".xml");
			if (!file.exists()) {
				throw new IOException();
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file.getPath());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("model");

			if (nList.getLength() == 0) {
				System.err.println("Invalid format for \"" + name + "\"");
				return;
			}

			Node nNode = nList.item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element modelNode = (Element) nNode, e;
				Element meshNode = (Element) modelNode.getElementsByTagName("mesh").item(0);
				StringTokenizer tok, tok2;

				//displayName = modelNode.getAttribute("name").trim();

				Element vertexNode = (Element) meshNode.getElementsByTagName("vertex").item(0);
				int vertexCount = Integer.parseInt(vertexNode.getAttribute("count"));
				mesh.vertexCount = vertexCount;

				Vertex vertices[] = new Vertex[vertexCount];

				for (int i = 0; i < vertexCount; i++) {
					vertices[i] = new Vertex();
				}

				tok = new StringTokenizer(vertexNode.getElementsByTagName("coord").item(0).getTextContent().trim(),
						"\n");
				for (int i = 0; i < vertexCount; i++) {
					tok2 = new StringTokenizer(tok.nextToken().trim(), " ");
					vertices[i].setXYZ(Float.parseFloat(tok2.nextToken()), Float.parseFloat(tok2.nextToken()),
							Float.parseFloat(tok2.nextToken()));
				}

				tok = new StringTokenizer(vertexNode.getElementsByTagName("normal").item(0).getTextContent().trim(),
						"\n");
				for (int i = 0; i < vertexCount; i++) {
					tok2 = new StringTokenizer(tok.nextToken().trim(), " ");
					vertices[i].setNXYZ(Float.parseFloat(tok2.nextToken()), Float.parseFloat(tok2.nextToken()),
							Float.parseFloat(tok2.nextToken()));
				}

				e = (Element) vertexNode.getElementsByTagName("uv").item(0);
				if (e != null) {
					tok = new StringTokenizer(e.getTextContent().trim(), "\n");
					for (int i = 0; i < vertexCount; i++) {
						tok2 = new StringTokenizer(tok.nextToken().trim(), " ");
						vertices[i].setUV(Float.parseFloat(tok2.nextToken()), Float.parseFloat(tok2.nextToken()));
					}
				}

				e = (Element) vertexNode.getElementsByTagName("color").item(0);
				if (e != null) {
					tok = new StringTokenizer(e.getTextContent().trim(), "\n");
					for (int i = 0; i < vertexCount; i++) {
						tok2 = new StringTokenizer(tok.nextToken().trim(), " ");
						vertices[i].setRGB(Float.parseFloat(tok2.nextToken()), Float.parseFloat(tok2.nextToken()),
								Float.parseFloat(tok2.nextToken()));
					}
				}
				mesh.vertices = vertices;

				Element faceNode = (Element) meshNode.getElementsByTagName("face").item(0);
				int indexCount = Integer.parseInt(faceNode.getAttribute("count")) * 3;
				mesh.indexCount = indexCount;
				int indexArray[] = new int[indexCount];

				tok = new StringTokenizer(faceNode.getTextContent().trim(), "\n");
				for (int i = 0; i < indexCount; i += 3) {
					tok2 = new StringTokenizer(tok.nextToken().trim(), " ");
					indexArray[i] = Integer.parseInt(tok2.nextToken());
					indexArray[i + 1] = Integer.parseInt(tok2.nextToken());
					indexArray[i + 2] = Integer.parseInt(tok2.nextToken());
				}
				mesh.indexArray = indexArray;

				e = (Element) modelNode.getElementsByTagName("position").item(0);
				if (e != null) {
					tok = new StringTokenizer(e.getTextContent().trim());
					if (tok.countTokens() >= 3) {
						position.x = Float.parseFloat(tok.nextToken());
						position.y = Float.parseFloat(tok.nextToken());
						position.z = Float.parseFloat(tok.nextToken());
					}
				}

				e = (Element) modelNode.getElementsByTagName("rotation").item(0);
				if (e != null) {
					tok = new StringTokenizer(e.getTextContent().trim());
					if (tok.countTokens() >= 3) {
						rotation.x = Float.parseFloat(tok.nextToken());
						rotation.y = Float.parseFloat(tok.nextToken());
						rotation.z = Float.parseFloat(tok.nextToken());
					}
				}

				e = (Element) modelNode.getElementsByTagName("scale").item(0);
				if (e != null) {
					tok = new StringTokenizer(e.getTextContent().trim());
					if (tok.countTokens() >= 3) {
						scale.x = Float.parseFloat(tok.nextToken());
						scale.y = Float.parseFloat(tok.nextToken());
						scale.z = Float.parseFloat(tok.nextToken());
					}
				}

				material = new Material("textures/" + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not load \"" + name + "\"");
		}
	}

	public void render() {
		calculateModelMatrix();
		calculateNormalMatrix();

		Shader.setMNMatrices(modelMatrix, normalMatrix);
		Shader.setProgram("Pass");
		material.bind();
		mesh.render();
		Texture.unbind();
		Shader.setProgram("");
	}

	public void destroy() {
		material.destroy();
		mesh.destroy();
	}
}
