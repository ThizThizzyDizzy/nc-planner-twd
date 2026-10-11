package net.ncplanner.plannerator.android;
import android.opengl.GLES30;
import com.thizthizzydizzy.dizzyengine.platform.GLBackend;
import com.thizthizzydizzy.dizzyengine.platform.Buffers;
/** Android framework GLES backend for DizzyEngine's shared renderer. */
final class AndroidGL implements GLBackend {
    @Override public int glGenVertexArrays() { int[] value = new int[1]; GLES30.glGenVertexArrays(1, value, 0); return value[0]; }
    @Override public int glGenBuffers() { int[] value = new int[1]; GLES30.glGenBuffers(1, value, 0); return value[0]; }
    @Override public int glGenTextures() { int[] value = new int[1]; GLES30.glGenTextures(1, value, 0); return value[0]; }
    @Override public int glGenFramebuffers() { int[] value = new int[1]; GLES30.glGenFramebuffers(1, value, 0); return value[0]; }
    @Override public int glGenRenderbuffers() { int[] value = new int[1]; GLES30.glGenRenderbuffers(1, value, 0); return value[0]; }
    @Override public void glBindVertexArray(int array) { GLES30.glBindVertexArray(array); }
    @Override public void glBindBuffer(int target, int buffer) { GLES30.glBindBuffer(target, buffer); }
    @Override public void glBindBufferBase(int target, int index, int buffer) { GLES30.glBindBufferBase(target, index, buffer); }
    @Override public void glBufferData(int target, float[] data, int usage) { java.nio.FloatBuffer buffer = Buffers.createFloatBuffer(data.length); buffer.put(data).flip(); GLES30.glBufferData(target, data.length * 4, buffer, usage); }
    @Override public void glBufferData(int target, int[] data, int usage) { java.nio.IntBuffer buffer = Buffers.createIntBuffer(data.length); buffer.put(data).flip(); GLES30.glBufferData(target, data.length * 4, buffer, usage); }
    @Override public void glBufferData(int target, long size, int usage) { GLES30.glBufferData(target, Math.toIntExact(size), null, usage); }
    @Override public void glBufferSubData(int target, long offset, java.nio.FloatBuffer data) { GLES30.glBufferSubData(target, Math.toIntExact(offset), data.remaining() * 4, data); }
    @Override public void glEnableVertexAttribArray(int index) { GLES30.glEnableVertexAttribArray(index); }
    @Override public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) { GLES30.glVertexAttribPointer(index, size, type, normalized, stride, Math.toIntExact(pointer)); }
    @Override public void glDrawElements(int mode, int count, int type, long indices) { GLES30.glDrawElements(mode, count, type, Math.toIntExact(indices)); }
    @Override public void glDrawElementsInstanced(int mode, int count, int type, long indices, int instances) { GLES30.glDrawElementsInstanced(mode, count, type, Math.toIntExact(indices), instances); }
    @Override public void glDeleteBuffers(int buffer) { GLES30.glDeleteBuffers(1, new int[]{buffer}, 0); }
    @Override public void glDeleteVertexArrays(int array) { GLES30.glDeleteVertexArrays(1, new int[]{array}, 0); }
    @Override public void glDeleteTextures(int texture) { GLES30.glDeleteTextures(1, new int[]{texture}, 0); }
    @Override public void glDeleteFramebuffers(int framebuffer) { GLES30.glDeleteFramebuffers(1, new int[]{framebuffer}, 0); }
    @Override public void glDeleteRenderbuffers(int renderbuffer) { GLES30.glDeleteRenderbuffers(1, new int[]{renderbuffer}, 0); }
    @Override public void glBindTexture(int target, int texture) { GLES30.glBindTexture(target, texture); }
    @Override public void glTexParameteri(int target, int pname, int param) { GLES30.glTexParameteri(target, pname, param); }
    @Override public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, java.nio.ByteBuffer pixels) { GLES30.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); }
    @Override public void glGenerateMipmap(int target) { GLES30.glGenerateMipmap(target); }
    @Override public void glActiveTexture(int texture) { GLES30.glActiveTexture(texture); }
    @Override public void glBindFramebuffer(int target, int framebuffer) { GLES30.glBindFramebuffer(target, framebuffer); }
    @Override public void glBindRenderbuffer(int target, int renderbuffer) { GLES30.glBindRenderbuffer(target, renderbuffer); }
    @Override public void glRenderbufferStorage(int target, int internalformat, int width, int height) { GLES30.glRenderbufferStorage(target, internalformat, width, height); }
    @Override public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) { GLES30.glFramebufferTexture2D(target, attachment, textarget, texture, level); }
    @Override public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) { GLES30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer); }
    @Override public int glCheckFramebufferStatus(int target) { return GLES30.glCheckFramebufferStatus(target); }
    @Override public void glViewport(int x, int y, int width, int height) { GLES30.glViewport(x, y, width, height); }
    @Override public void glClearColor(float red, float green, float blue, float alpha) { GLES30.glClearColor(red, green, blue, alpha); }
    @Override public void glClear(int mask) { GLES30.glClear(mask); }
    @Override public void glEnable(int cap) { GLES30.glEnable(cap); }
    @Override public void glDisable(int cap) { GLES30.glDisable(cap); }
    @Override public boolean glIsEnabled(int cap) { return GLES30.glIsEnabled(cap); }
    @Override public void glBlendFunc(int sfactor, int dfactor) { GLES30.glBlendFunc(sfactor, dfactor); }
    @Override public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) { GLES30.glColorMask(red, green, blue, alpha); }
    @Override public void glDepthMask(boolean flag) { GLES30.glDepthMask(flag); }
    @Override public void glStencilMask(int mask) { GLES30.glStencilMask(mask); }
    @Override public void glStencilFunc(int func, int ref, int mask) { GLES30.glStencilFunc(func, ref, mask); }
    @Override public void glStencilOp(int sfail, int dpfail, int dppass) { GLES30.glStencilOp(sfail, dpfail, dppass); }
    @Override public void glScissor(int x, int y, int width, int height) { GLES30.glScissor(x, y, width, height); }
    @Override public int glGetInteger(int pname) { int[] value=new int[1]; GLES30.glGetIntegerv(pname,value,0); return value[0]; }
    @Override public void glGetIntegerv(int pname, int[] params) { GLES30.glGetIntegerv(pname, params, 0); }
    @Override public void glReadPixels(int x, int y, int width, int height, int format, int type, java.nio.ByteBuffer pixels) { GLES30.glReadPixels(x, y, width, height, format, type, pixels); }
    @Override public int glCreateShader(int type) { return GLES30.glCreateShader(type); }
    @Override public void glShaderSource(int shader, CharSequence string) { GLES30.glShaderSource(shader, string.toString()); }
    @Override public void glCompileShader(int shader) { GLES30.glCompileShader(shader); }
    @Override public void glGetShaderiv(int shader, int pname, int[] params) { GLES30.glGetShaderiv(shader, pname, params, 0); }
    @Override public String glGetShaderInfoLog(int shader) { return GLES30.glGetShaderInfoLog(shader); }
    @Override public int glCreateProgram() { return GLES30.glCreateProgram(); }
    @Override public void glAttachShader(int program, int shader) { GLES30.glAttachShader(program, shader); }
    @Override public void glLinkProgram(int program) { GLES30.glLinkProgram(program); }
    @Override public void glGetProgramiv(int program, int pname, int[] params) { GLES30.glGetProgramiv(program, pname, params, 0); }
    @Override public String glGetProgramInfoLog(int program) { return GLES30.glGetProgramInfoLog(program); }
    @Override public void glUseProgram(int program) { GLES30.glUseProgram(program); }
    @Override public int glGetUniformLocation(int program, CharSequence name) { return GLES30.glGetUniformLocation(program, name.toString()); }
    @Override public void glUniform1f(int location, float v0) { GLES30.glUniform1f(location, v0); }
    @Override public void glUniform2f(int location, float v0, float v1) { GLES30.glUniform2f(location, v0, v1); }
    @Override public void glUniform3f(int location, float v0, float v1, float v2) { GLES30.glUniform3f(location, v0, v1, v2); }
    @Override public void glUniform4f(int location, float v0, float v1, float v2, float v3) { GLES30.glUniform4f(location, v0, v1, v2, v3); }
    @Override public void glUniform1i(int location, int v0) { GLES30.glUniform1i(location, v0); }
    @Override public void glUniformMatrix4fv(int location, boolean transpose, float[] value) { GLES30.glUniformMatrix4fv(location, 1, transpose, value, 0); }
    @Override public void glDeleteShader(int shader) { GLES30.glDeleteShader(shader); }
    @Override public void glDeleteProgram(int program) { GLES30.glDeleteProgram(program); }
}
