package org.devvictor.infra.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.devvictor.application.enums.HttpStatus;
import org.devvictor.application.exceptions.BadRequestException;
import org.devvictor.application.exceptions.ExceptionResponse;
import org.devvictor.application.services.UserService;
import org.devvictor.domain.dtos.CreateUserRequestDTO;
import org.devvictor.domain.dtos.UpdateUserRequestDTO;
import org.devvictor.infra.daos.UserPostgresDAO;

import org.devvictor.application.exceptions.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/users/*")
public class UserController extends HttpServlet {
    private UserService userService;
    private Gson mapper;

    @Override
    public void init() {
        this.userService = new UserService(new UserPostgresDAO());
        this.mapper = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            var createUserRequestDTO = mapper.fromJson(builder.toString(), CreateUserRequestDTO.class);

            userService.create(createUserRequestDTO);

            response.setStatus(HttpStatus.CREATED.getCode());
        } catch (ApplicationException exception) {
            handleErrors(request, response, exception);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null) throw new BadRequestException("User id is required on path");

            String[] pathParts = pathInfo.split("/");
            int userId = Integer.parseInt(pathParts[1]);

            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            var updateUserRequestDTO = mapper.fromJson(builder.toString(), UpdateUserRequestDTO.class);

            userService.update(userId, updateUserRequestDTO);
        } catch (ApplicationException exception) {
            handleErrors(request, response, exception);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String page = request.getParameter("page");
            String itemsPerPage = request.getParameter("itemsPerPage");

            if (page == null || itemsPerPage == null) {
                throw new BadRequestException("Missing page or itemsPerPage query params");
            }

            var users = userService.find(Integer.parseInt(page), Integer.parseInt(itemsPerPage));

            response.getWriter().write(mapper.toJson(users));
        } catch (ApplicationException exception) {
            handleErrors(request, response, exception);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null) throw new BadRequestException("User id is required on path");

            String[] pathParts = pathInfo.split("/");
            int userId = Integer.parseInt(pathParts[1]);

            userService.delete(userId);
        } catch (ApplicationException exception) {
            handleErrors(request, response, exception);
        }
    }

    private void handleErrors(HttpServletRequest request,
                              HttpServletResponse response,
                              ApplicationException exception) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(exception.getStatus());
        response.getWriter().write(mapper.toJson(
                new ExceptionResponse(exception.getStatus(), exception.getMessage())
        ));
    }
}
