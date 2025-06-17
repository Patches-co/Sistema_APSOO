/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import projeto.db.ConexaoDB;
import projeto.model.Usuario;

/**
 *
 * @author Jvvpa
 */
public class UsuarioDAO {
    public void salvar(Usuario usuario) throws SQLException{
        String sql = "INSERT INTO usuarios (nome_completo, email, senha, unidade_apartamento, tipo_usuario, telefone, cpf) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setString(4, usuario.getUnidade());
            pstmt.setString(5, usuario.getTipoUsuario());
            pstmt.setString(6, usuario.getTelefone());
            pstmt.setString(7, usuario.getCPF());
            
            pstmt.executeUpdate();
            System.out.println("Usuário salvo com sucesso!");
        }
    }
    
    // busca por email
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setUnidade(rs.getString("unidade_apartamento"));
                    usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                    usuario.setTelefone(rs.getString("telefone"));
                    usuario.setCPF(rs.getString("cpf"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por e-mail: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }
    
    // busca por nome
    public List<Usuario> buscarMoradoresPorNome(String nome) {
        String sql = "SELECT * FROM usuarios WHERE tipo_usuario = 'morador' AND nome_completo ILIKE ? ORDER BY nome_completo";
        List<Usuario> moradores = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nome + "%");
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setEmail(rs.getString("email"));
                usuario.setUnidade(rs.getString("unidade_apartamento"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCPF(rs.getString("cpf"));
                moradores.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moradores;
    }
    
    // listar
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY nome_completo";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setEmail(rs.getString("email"));
                usuario.setUnidade(rs.getString("unidade_apartamento"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCPF(rs.getString("cpf"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    public List<Usuario> listarMoradores() {
        System.out.println("--- [DEBUG UsuarioDAO] Buscando lista de moradores... ---");
        String sql = "SELECT * FROM usuarios WHERE tipo_usuario = 'morador' ORDER BY nome_completo";
        List<Usuario> moradores = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("--- [DEBUG UsuarioDAO] Query executada com sucesso. ---");
            while (rs.next()) {
                System.out.println("--- [DEBUG UsuarioDAO] Encontrei o morador: " + rs.getString("nome_completo") + " ---");
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setEmail(rs.getString("email"));
                usuario.setUnidade(rs.getString("unidade_apartamento"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCPF(rs.getString("cpf"));
                moradores.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("!!! [DEBUG UsuarioDAO] ERRO ao listar moradores: " + e.getMessage() + " !!!");
            e.printStackTrace();
        }
        System.out.println("--- [DEBUG UsuarioDAO] Finalizando. Total de moradores encontrados: " + moradores.size() + " ---");
        return moradores;
    }
    
    // atualizar
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome_completo = ?, email = ?, unidade_apartamento = ?, cpf = ?, telefone = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getUnidade());
            pstmt.setString(4, usuario.getCPF());
            pstmt.setString(5, usuario.getTelefone());
            pstmt.setInt(6, usuario.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    // deletar
    public void deletar(int idUsuario) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        }
    }
}
