#define GLEW_STATIC
#include <GL/glew.h>
#include <GLFW/glfw3.h>

#include "glm/glm.hpp" 
#include "glm/gtc/matrix_transform.hpp" 
#include "glm/gtc/matrix_inverse.hpp" 
#include "glm/gtc/type_ptr.hpp" 

#include "Window.h"
#include "Shader.hpp"
#include "Camera.hpp"
#include "Model3D.hpp"

#include <iostream>

gps::Model3D sky;
gps::Model3D scena;
gps::Model3D bench;
gps::Model3D lamp;
gps::Model3D crow;
gps::Model3D wL;
gps::Model3D wR;
gps::Model3D drop;
gps::Window myWindow;
glm::mat4 model;
glm::mat4 view;
glm::mat4 projection;
glm::mat3 normalMatrix;

glm::vec3 lightDir;
glm::vec3 lightColor;

glm::vec3 pLightPos;
GLuint modelLoc;
GLuint viewLoc;
GLuint projectionLoc;
GLuint normalMatrixLoc;
GLuint lightDirLoc;
GLuint lightColorLoc;
unsigned int depthCubemap;
GLuint depthMapFBO;
GLuint pLightPosLoc;

const unsigned int SHADOW_WIDTH = 2048;
const unsigned int SHADOW_HEIGHT = 2048;
GLuint shadowMapFBO;
GLuint depthMapTexture;

gps::Camera myCamera(
	glm::vec3(-3.74433f, 1.60775f, 1.44585f),
	glm::vec3(-0.943888f, 1.60775f, 1.7225f),
	glm::vec3(0.0f, 1.0f, 0.0f));

GLfloat cameraSpeed = 0.1f;

GLboolean pressedKeys[1024];


GLfloat angle;

float crowY = 0.092817f;
float crowZ = 0.655062f;
float wLY = 0.083589f;
float wLZ = 0.647693f;
float wRY = 0.083822f;
float wRZ = 0.645177f;
bool wUp;
float wAngle = 0;


gps::Shader BasicShader;


int changeLight = 0; 
int fog = 0;

bool wireframe = false;
bool wind = false;
bool rain = false;
std::vector<glm::vec3> dropsInitialPos;
std::vector<glm::vec3> dropsPos;
float dropZ;

GLenum glCheckError_(const char* file, int line)
{
	GLenum errorCode;
	while ((errorCode = glGetError()) != GL_NO_ERROR) {
		std::string error;
		switch (errorCode) {
		case GL_INVALID_ENUM:
			error = "INVALID_ENUM";
			break;
		case GL_INVALID_VALUE:
			error = "INVALID_VALUE";
			break;
		case GL_INVALID_OPERATION:
			error = "INVALID_OPERATION";
			break;
		case GL_STACK_OVERFLOW:
			error = "STACK_OVERFLOW";
			break;
		case GL_STACK_UNDERFLOW:
			error = "STACK_UNDERFLOW";
			break;
		case GL_OUT_OF_MEMORY:
			error = "OUT_OF_MEMORY";
			break;
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			error = "INVALID_FRAMEBUFFER_OPERATION";
			break;
		}
		std::cout << error << " | " << file << " (" << line << ")" << std::endl;
	}
	return errorCode;
}
#define glCheckError() glCheckError_(__FILE__, __LINE__)

void windowResizeCallback(GLFWwindow* window, int width, int height) {
	fprintf(stdout, "Window resized! New width: %d , and height: %d\n", width, height);
	//TODO
}

void keyboardCallback(GLFWwindow* window, int key, int scancode, int action, int mode) {
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
		glfwSetWindowShouldClose(window, GL_TRUE);
	}

	if (key >= 0 && key < 1024) {
		if (action == GLFW_PRESS) {
			pressedKeys[key] = true;
		}
		else if (action == GLFW_RELEASE) {
			pressedKeys[key] = false;
		}
	}
}

void initOpenGLWindow() {
	myWindow.Create(1024, 768, "OpenGL Project Core");
}

float xpos0 = 0, ypos0 = 0;
float mouseSpeed = 0.02f;
GLboolean begin = true;
void mouseCallback(GLFWwindow* window, double xpos, double ypos) {
	//TODO
	if (begin) {
		xpos0 = xpos;
		ypos0 = ypos;
		begin = false;
	}
	else {
		double deltaX = xpos - xpos0;
		double deltaY = ypos - ypos0;
		myCamera.rotate(-1 * deltaY * mouseSpeed, deltaX * mouseSpeed);
		view = myCamera.getViewMatrix();
		BasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));

		xpos0 = xpos;
		ypos0 = ypos;
	}
}
void setWindowCallbacks() {
	glfwSetWindowSizeCallback(myWindow.getWindow(), windowResizeCallback);
	glfwSetKeyCallback(myWindow.getWindow(), keyboardCallback);
	glfwSetCursorPosCallback(myWindow.getWindow(), mouseCallback);
}

void initOpenGLState() {
	glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
	glViewport(0, 0, myWindow.getWindowDimensions().width, myWindow.getWindowDimensions().height);
	glEnable(GL_FRAMEBUFFER_SRGB);
	glEnable(GL_DEPTH_TEST); 
	glDepthFunc(GL_LESS); 
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK); 
	glFrontFace(GL_CCW); 
}



void initModels() {
	sky.LoadModel("models/sky/skyTest.obj");
	scena.LoadModel("models/gate+ground/ground.obj");
	lamp.LoadModel("models/street-lamp/lamp.obj");
	bench.LoadModel("models/bench/bench.obj");
	crow.LoadModel("models/bodyCrow/body.obj");
	wL.LoadModel("models/wingL/wingL.obj");
	wR.LoadModel("models/wingR/wingR.obj");
	drop.LoadModel("models/raindrop/drop.obj");
}

void initShaders() {
	BasicShader.loadShader("shaders/basic.vert", "shaders/basic.frag");
	

}

void initUniforms() {
	BasicShader.useShaderProgram();
	model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
	modelLoc = glGetUniformLocation(BasicShader.shaderProgram, "model");
	view = myCamera.getViewMatrix();
	viewLoc = glGetUniformLocation(BasicShader.shaderProgram, "view");
	glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

	normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	normalMatrixLoc = glGetUniformLocation(BasicShader.shaderProgram, "normalMatrix");

	projection = glm::perspective(glm::radians(45.0f), (float)myWindow.getWindowDimensions().width / (float)myWindow.getWindowDimensions().height, 0.1f, 500.0f);
	projectionLoc = glGetUniformLocation(BasicShader.shaderProgram, "projection");
	glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));

	lightDir = glm::vec3(0.0f, 7.0f, 1.0f);
	lightDirLoc = glGetUniformLocation(BasicShader.shaderProgram, "lightDir");
	glUniform3fv(lightDirLoc, 1, glm::value_ptr(lightDir));
	lightColor = glm::vec3(1.0f, 1.0f, 1.0f);
	lightColorLoc = glGetUniformLocation(BasicShader.shaderProgram, "lightColor");
	glUniform3fv(lightColorLoc, 1, glm::value_ptr(lightColor));
}

void initFBO() {
	glGenFramebuffers(1, &shadowMapFBO);
	glGenTextures(1, &depthMapTexture);
	glBindTexture(GL_TEXTURE_2D, depthMapTexture);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
		SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
	glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMapTexture, 0);

	glDrawBuffer(GL_NONE);
	glReadBuffer(GL_NONE);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

GLuint ReadTextureFromFile(const char* file_name) {
	int x, y, n;
	int force_channels = 4;
	unsigned char* image_data = stbi_load(file_name, &x, &y, &n, force_channels);
	if (!image_data) {
		fprintf(stderr, "ERROR: could not load %s\n", file_name);
		return false;
	}
	// NPOT check
	if ((x & (x - 1)) != 0 || (y & (y - 1)) != 0) {
		fprintf(
			stderr, "WARNING: texture %s is not power-of-2 dimensions\n", file_name
		);
	}

	int width_in_bytes = x * 4;
	unsigned char* top = NULL;
	unsigned char* bottom = NULL;
	unsigned char temp = 0;
	int half_height = y / 2;

	for (int row = 0; row < half_height; row++) {
		top = image_data + row * width_in_bytes;
		bottom = image_data + (y - row - 1) * width_in_bytes;
		for (int col = 0; col < width_in_bytes; col++) {
			temp = *top;
			*top = *bottom;
			*bottom = temp;
			top++;
			bottom++;
		}
	}

	GLuint textureID;
	glGenTextures(1, &textureID);
	glBindTexture(GL_TEXTURE_2D, textureID);
	glTexImage2D(
		GL_TEXTURE_2D,
		0,
		GL_SRGB, //GL_SRGB,//GL_RGBA,
		x,
		y,
		0,
		GL_RGBA,
		GL_UNSIGNED_BYTE,
		image_data
	);
	glGenerateMipmap(GL_TEXTURE_2D);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glBindTexture(GL_TEXTURE_2D, 0);

	return textureID;
}

void initCubeMap() {
	glGenTextures(1, &depthCubemap);
	glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubemap);
	for (unsigned int i = 0; i < 6; ++i)
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
	glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
	glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthCubemap, 0);
	glDrawBuffer(GL_NONE);
	glReadBuffer(GL_NONE);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}
void renderScene();
void sceneAnimation() {

	std::vector<glm::vec3> path;
	glm::vec3 targetPos = glm::vec3(8.6625f, 1.81263f, 2.37074f);

	path.push_back(glm::vec3(0.85717f, 4.0657f, -5.00509f));
	float z = -5.00509f;
	while (z < 9.81967f) {
		z += 0.1f;
		path.push_back(glm::vec3(0.85717f, 4.0657f, z));
	}

	path.push_back(glm::vec3(0.85717f, 4.0657f, 9.81967f));
	float x = 0.85717f;
	while (x < 15.7986f) {
		x += 0.1f;
		path.push_back(glm::vec3(x, 4.0657f, 9.81967f));
	}

	path.push_back(glm::vec3(15.7986f, 4.0657f, 9.81967f));
	z = 9.81967f;
	while (z > -3.59243f) {
		z -= 0.1f;
		path.push_back(glm::vec3(15.7986f, 4.0657f, z));
	}

	path.push_back(glm::vec3(15.7986f, 4.0657f, -3.59243f));
	x = 15.7986f;
	while (x > 0.85717f) {
		x -= 0.1f;
		path.push_back(glm::vec3(x, 4.0657f, -3.59243f));
	}


	while (path.size())	{
		myCamera = gps::Camera(path.back(),targetPos,glm::vec3(0.0f, 1.0f, 0.0f));

		path.pop_back();

		renderScene();
		glfwPollEvents();
		glfwSwapBuffers(myWindow.getWindow());

	}
}

void initRain() {
	for (int i = 0; i < 3000; i++) {
		float initialX = (rand() % 14476 + 1874) / 1000.0f;
		float initialY = ((rand() % 18304) - 11712) / 1000.0f;
		float initialZ = (rand() % 8081) / 1000.0f;
		dropsInitialPos.push_back(glm::vec3(initialX, initialZ, -initialY));
		dropsPos.push_back(glm::vec3(initialX, initialZ, -initialY));
	}
}

void processMovement() {
	if (pressedKeys[GLFW_KEY_W]) {
		myCamera.move(gps::MOVE_FORWARD, cameraSpeed);
		view = myCamera.getViewMatrix();
		BasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_S]) {
		myCamera.move(gps::MOVE_BACKWARD, cameraSpeed);
		view = myCamera.getViewMatrix();
		BasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_A]) {
		myCamera.move(gps::MOVE_LEFT, cameraSpeed);
		view = myCamera.getViewMatrix();
		BasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_D]) {
		myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
		view = myCamera.getViewMatrix();
		BasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_T]) {
		sceneAnimation();
	}

	if (pressedKeys[GLFW_KEY_M]) {
		wireframe = !wireframe;
		if (wireframe) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		}
		else {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
	}

	if (pressedKeys[GLFW_KEY_L]) {
		if (changeLight == 1) {
			changeLight = 0;
		}
		else {
			changeLight = 1;
		}
		BasicShader.useShaderProgram();
		glUniform1i(glGetUniformLocation(BasicShader.shaderProgram, "changeLight"), changeLight);
	}

	if (pressedKeys[GLFW_KEY_F]) {
		if (fog == 1) {
			fog = 0;
		}
		else {
			fog = 1;
		}
		BasicShader.useShaderProgram();
		glUniform1i(glGetUniformLocation(BasicShader.shaderProgram, "fog"), fog);
	}

	if (pressedKeys[GLFW_KEY_C]) {
		crowY += 0.01f;
		crowZ += 0.01f;
		wLY += 0.01f;
		wLZ += 0.01f;
		wRY += 0.01f;
		wRZ += 0.01f;

		if (wUp) {
			wAngle += 0.001f;
		}
		else {
			wAngle -= 0.001f;
		}

		if (wAngle >= 0.01) {
			wUp = !wUp;
		}
		
		if (wAngle <= -0.01) {
			wUp = !wUp;
		}
	}

	if (pressedKeys[GLFW_KEY_Z]) {
			initRain();
		
	}
	if (pressedKeys[GLFW_KEY_X]) {
		wind = !wind;
	}
}



void renderScena(gps::Shader shader, bool depthPass) {
	shader.useShaderProgram();
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

	if (depthPass == false) {
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}
	scena.Draw(shader);
}

void renderSky(gps::Shader shader,bool depthPass) {
	shader.useShaderProgram();
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));
	if (depthPass == false) {
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}

	sky.Draw(shader);
}

void renderBench(gps::Shader shader, bool depthPass) {
	shader.useShaderProgram();

	glm::mat4 modelBench = glm::mat4(1.0f);
	modelBench = glm::translate(modelBench, glm::vec3(4.31311f, -0.000201f, 1.25905f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelBench));

	if (!depthPass) {
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}

	bench.Draw(shader);
}

void renderLamp(gps::Shader shader, bool depthPass) {
	
	shader.useShaderProgram();
	glm::mat4 modelLamp = glm::mat4(1.0f);
	modelLamp = glm::translate(modelLamp, glm::vec3(3.7833f, -0.019674f, 3.02676f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelLamp));

	if (!depthPass) {
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}
	lamp.Draw(shader);
}

void renderCrow(gps::Shader shader, bool depthPass) {
	
	shader.useShaderProgram();
	glm::mat4 modelBodyCrow = glm::mat4(1.0f);
	modelBodyCrow = glm::translate(modelBodyCrow, glm::vec3(5.9248f, crowY, crowZ));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelBodyCrow));

	if (!depthPass) {
		
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}
	crow.Draw(shader);
}

void renderWL(gps::Shader shader, bool depthPass) {
	
	shader.useShaderProgram();
	glm::mat4 modelWingL = glm::mat4(1.0f);
	modelWingL = glm::translate(modelWingL, glm::vec3(5.94813f, wLY, wLZ));
	modelWingL = glm::rotate(modelWingL, wAngle, glm::vec3(0.0f, 0.0f, 1.0f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelWingL));

	if (!depthPass) {
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}
	wL.Draw(shader);
}

void renderWR(gps::Shader shader, bool depthPass) {
	shader.useShaderProgram();
	glm::mat4 modelWingR = glm::mat4(1.0f);

	modelWingR = glm::translate(modelWingR, glm::vec3(5.89672f, wRY, wRZ));
	modelWingR = glm::rotate(modelWingR, wAngle, glm::vec3(0.0f, 0.0f, 1.0f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelWingR));

	if (!depthPass) {
		glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}
	wR.Draw(shader);
}

void renderRain(gps::Shader shader, bool depthPass) {
	for (int i = 0; i < 3000; i++) {
		shader.useShaderProgram();

		glm::mat4 modelRaindrop = glm::mat4(1.0f);
		glm::vec3 raindropPos = dropsPos.at(i);

		modelRaindrop = glm::translate(modelRaindrop, raindropPos);
		dropsPos.at(i).y -= 0.015;
		if (wind) {
			modelRaindrop = glm::rotate(modelRaindrop, 0.1f, glm::vec3(1.0f, 0.0f, 0.0f));
			dropsPos.at(i).z -= 0.02;
		}
		glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelRaindrop));

		if (!depthPass) {
			glUniformMatrix3fv(glGetUniformLocation(shader.shaderProgram, "normalMatrix"), 1, GL_FALSE, glm::value_ptr(normalMatrix));
		}
		drop.Draw(shader);
	}

	
}


glm::mat4 computeLightSpaceTrMatrix() {
	glm::mat4 lightView = glm::lookAt(lightDir, glm::vec3(12.0f, 0.0f, 0.0f), glm::vec3(0.0f, 1.0f, 0.0f));
	const GLfloat near_plane = 5.0f, far_plane = 20.0f;
	glm::mat4 lightProjection = glm::ortho(-7.0f, 7.0f, -7.0f, 7.0f, near_plane, far_plane);
	glm::mat4 lightSpaceTrMatrix = lightProjection * lightView;
	
	return lightSpaceTrMatrix;
}

void renderScene() {


	glBindFramebuffer(GL_FRAMEBUFFER, 0);

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	BasicShader.useShaderProgram();

	glUniformMatrix4fv(glGetUniformLocation(BasicShader.shaderProgram, "lightSpaceTrMatrix"),1,GL_FALSE,glm::value_ptr(computeLightSpaceTrMatrix()));
	view = myCamera.getViewMatrix();
	glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

	glViewport(0, 0, (float)myWindow.getWindowDimensions().width, (float)myWindow.getWindowDimensions().height);
	BasicShader.useShaderProgram();
	renderScena(BasicShader, false);
	renderSky(BasicShader,false);
	renderLamp(BasicShader, false);
	renderBench(BasicShader, false);
	renderCrow(BasicShader, false);
	renderWL(BasicShader, false);
	renderWR(BasicShader, false);
	if (rain) {
		renderRain(BasicShader, false);
	}

	pLightPos = glm::vec3(3.77206f, 0.789307f, 2.86863f);
	pLightPosLoc = glGetUniformLocation(BasicShader.shaderProgram, "pLightPosition");
	glUniform3fv(pLightPosLoc, 1, glm::value_ptr(pLightPos));

}

void cleanup() {
	glDeleteTextures(1, &depthMapTexture);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	glDeleteFramebuffers(1, &shadowMapFBO);
	myWindow.Delete();
}

int main(int argc, const char* argv[]) {

	try {
		initOpenGLWindow();
	}
	catch (const std::exception & e) {
		std::cerr << e.what() << std::endl;
		return EXIT_FAILURE;
	}
	initOpenGLState();
	initFBO();
	initModels();
	initShaders();
	initUniforms();
	setWindowCallbacks();
	glCheckError();
	while (!glfwWindowShouldClose(myWindow.getWindow())) {
		processMovement();
		renderScene();

		glfwPollEvents();
		glfwSwapBuffers(myWindow.getWindow());

		glCheckError();
	}
	cleanup();
	return EXIT_SUCCESS;
}
