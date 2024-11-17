package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.sql.SQLOutput;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    // everything will be private, so we can control whatever happens when these are changed.
    private int width, height;
    private String title;
    private long glfwWindow;

    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null; // create a singleton (so we always only have 1 instance of a window).

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    // creating the get() function.
    /*
        - this is written, so we only have one window
        - not multiple for every time we try to open it.
        - it will run every time for the Main(7th line) to check if the window exists or not.
     */
    public static Window get() {  // over here the return type is Window, coz that's what we need it to return.
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
//        glfwFreeCallBacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate the GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialise GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        /*
            - window hints are basic, do you want it to be resizable, do you want it to close
            - things like this
         */
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // This will make sure when the window starts it's in to maximize pos

        // Create the Window
        // now glfw will use this instances to create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);// SELECTED MEMORYUTIL.NULL
        /*
            - so the above returns a "long"
            - this long is the memory address where this window is in the memory space
         */
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to Create GLFW WINDOW.");
        }

        // :: is java shorthand for lambda function
        // lambdas are just functions which says forward this thing to this function
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener :: keyCallback);

        // Make the Open GL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync - basically buffer swapping
        glfwSwapInterval(1);
        /*
            - means you have no restriction swap every single frame
            - this swap interval says how much time we need to wait and in this case just do it doesn't need to wait
         */

        // Make the Window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (fadeToBlack) {
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
//                a = Math.max(a - 0.01f, 0);

            }
            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                fadeToBlack = true;
            }
            glfwSwapBuffers(glfwWindow);
        }
    }
}
