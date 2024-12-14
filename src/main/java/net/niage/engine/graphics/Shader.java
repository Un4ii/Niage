package net.niage.engine.graphics;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;

import net.niage.engine.utils.FileUtils;

public class Shader {

    private int ID;

    private int vertex, fragment;
    private String vertexString, fragmentString;

    public Shader(String vertexPath, String fragmentPath) throws Exception {
        vertexString = FileUtils.readFile(vertexPath);
        fragmentString = FileUtils.readFile(fragmentPath);

        init();
    }

    private void init() throws Exception {
        vertex = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertex, vertexString);
        GL20.glCompileShader(vertex);
        if (GL20.glGetShaderi(vertex, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            int logLength = GL20.glGetShaderi(vertex, GL20.GL_INFO_LOG_LENGTH);
            String infoLog = GL20.glGetShaderInfoLog(vertex, logLength);
            throw new RuntimeException("ERROR::SHADER::VERTEX::COMPILATION\n" + infoLog);
        }

        fragment = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragment, fragmentString);
        GL20.glCompileShader(fragment);
        if (GL20.glGetShaderi(fragment, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            int logLength = GL20.glGetShaderi(fragment, GL20.GL_INFO_LOG_LENGTH);
            String infoLog = GL20.glGetShaderInfoLog(fragment, logLength);
            throw new RuntimeException("ERROR::SHADER::FRAGMENT::COMPILATION\n" + infoLog);
        }

        ID = GL20.glCreateProgram();
        GL20.glAttachShader(ID, vertex);
        GL20.glAttachShader(ID, fragment);
        GL20.glLinkProgram(ID);
        if (GL20.glGetProgrami(ID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            int logLength = GL20.glGetProgrami(ID, GL20.GL_INFO_LOG_LENGTH);
            String infoLog = GL20.glGetProgramInfoLog(ID, logLength);
            throw new RuntimeException("ERROR::SHADER::PROGRAM::LINKING_FAILED\n" + infoLog);
        }

        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);

        // Bind Camera Matrices
        int cameraUniformIndex = GL31.glGetUniformBlockIndex(ID, "PerspectiveCamera");
        if (cameraUniformIndex != -1) {
            GL31.glUniformBlockBinding(ID, cameraUniformIndex, 0);
        } else {
            System.err.println("WARNING::SHADER::UBO::PERSPECTIVE\nCamera uniform not found");
        }

        cameraUniformIndex = GL31.glGetUniformBlockIndex(ID, "OrthographicCamera");
        if (cameraUniformIndex != -1) {
            GL31.glUniformBlockBinding(ID, cameraUniformIndex, 0);
        } else {
            System.err.println("WARNING::SHADER::UBO::ORTHOGRAPHIC\nCamera uniform not found");
        }
    }

    public void bind() {
        GL20.glUseProgram(ID);
    }

    public static void unbind() {
        GL20.glUseProgram(0);
    }

    public void dispose() {
        GL20.glDeleteProgram(ID);
    }

    public int ID() {
        return ID;
    }

    // Set uniform Bool
    public void setBool(String name, boolean value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(ID, name), value ? 1 : 0);
    }

    public void setBool2(String name, boolean value1, boolean value2) {
        GL20.glUniform2i(GL20.glGetUniformLocation(ID, name), value1 ? 1 : 0, value2 ? 1 : 0);
    }

    public void setBool3(String name, boolean value1, boolean value2, boolean value3) {
        GL20.glUniform3i(GL20.glGetUniformLocation(ID, name), value1 ? 1 : 0, value2 ? 1 : 0, value3 ? 1 : 0);
    }

    public void setBool4(String name, boolean value1, boolean value2, boolean value3, boolean value4) {
        GL20.glUniform4i(GL20.glGetUniformLocation(ID, name), value1 ? 1 : 0, value2 ? 1 : 0, value3 ? 1 : 0,
                value4 ? 1 : 0);
    }

    // Set uniform Int
    public void setInt(String name, int value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(ID, name), value);
    }

    public void setInt2(String name, int value1, int value2) {
        GL20.glUniform2i(GL20.glGetUniformLocation(ID, name), value1, value2);
    }

    public void setInt3(String name, int value1, int value2, int value3) {
        GL20.glUniform3i(GL20.glGetUniformLocation(ID, name), value1, value2, value3);
    }

    public void setInt4(String name, int value1, int value2, int value3, int value4) {
        GL20.glUniform4i(GL20.glGetUniformLocation(ID, name), value1, value2, value3, value4);
    }

    // Set unifrom Float
    public void setFloat(String name, float value) {
        GL20.glUniform1f(GL20.glGetUniformLocation(ID, name), value);
    }

    public void setFloat2(String name, float value1, float value2) {
        GL20.glUniform2f(GL20.glGetUniformLocation(ID, name), value1, value2);
    }

    public void setFloat3(String name, float value1, float value2, float value3) {
        GL20.glUniform3f(GL20.glGetUniformLocation(ID, name), value1, value2, value3);
    }

    public void setFloat4(String name, float value1, float value2, float value3, float value4) {
        GL20.glUniform4f(GL20.glGetUniformLocation(ID, name), value1, value2, value3, value4);
    }

    // Set uniform Matrices
    public void setMat2(String name, Matrix2f matrix) {
        int location = GL20.glGetUniformLocation(ID, name);
        float[] matrixBuffer = new float[4]; // 2x2 matrix has 4 values
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix2fv(location, false, matrixBuffer);
    }

    public void setMat3(String name, Matrix3f matrix) {
        int location = GL20.glGetUniformLocation(ID, name);
        float[] matrixBuffer = new float[9]; // 3x3 matrix has 9 values
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix3fv(location, false, matrixBuffer);
    }

    public void setMat4(String name, Matrix4f matrix) {
        int location = GL20.glGetUniformLocation(ID, name);
        float[] matrixBuffer = new float[16]; // 4x4 matrix has 16 values
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    // Set uniform Vectors
    public void setVec2(String name, Vector2f vector) {
        int location = GL20.glGetUniformLocation(ID, name);
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    public void setVec3(String name, Vector3f vector) {
        int location = GL20.glGetUniformLocation(ID, name);
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    public void setVec4(String name, Vector4f vector) {
        int location = GL20.glGetUniformLocation(ID, name);
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    public void setVec2(String name, float value1, float value2) {
        GL20.glUniform2f(GL20.glGetUniformLocation(ID, name), value1, value2);
    }

    public void setVec3(String name, float value1, float value2, float value3) {
        GL20.glUniform3f(GL20.glGetUniformLocation(ID, name), value1, value2, value3);
    }

    public void setVec4(String name, float value1, float value2, float value3, float value4) {
        GL20.glUniform4f(GL20.glGetUniformLocation(ID, name), value1, value2, value3, value4);
    }

}
