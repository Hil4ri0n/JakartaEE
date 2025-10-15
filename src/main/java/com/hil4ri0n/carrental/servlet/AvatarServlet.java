package com.hil4ri0n.carrental.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/api/avatars/*")
@MultipartConfig
public class AvatarServlet extends HttpServlet {

    private Path avatarDir;

    @Override
    public void init() throws ServletException {
        avatarDir = Paths.get(getServletContext().getInitParameter("avatarDir"));
        try {
            Files.createDirectories(avatarDir);
        } catch (IOException e) {
            throw new ServletException("Cannot create avatar directory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getPathInfo().substring(1);
        Path file = avatarDir.resolve(id + ".png");
        if (Files.exists(file)) {
            resp.setContentType("image/png");
            Files.copy(file, resp.getOutputStream());
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getPathInfo().substring(1);
        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Path file = avatarDir.resolve(id + ".png");
        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getPathInfo().substring(1);
        Path file = avatarDir.resolve(id + ".png");
        if (Files.deleteIfExists(file)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
