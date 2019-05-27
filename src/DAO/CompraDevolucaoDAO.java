package DAO;

import VO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Database;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraDevolucaoDAO extends PadraoDAO {

    public CompraDevolucaoDAO(boolean devolucao) {
        if(devolucao)
            tabela = "DEVOLUCAO";
        else
            tabela = "COMPRA";
        conn = Database.getConnection();
    }

    @Override
    public boolean Inserir(PadraoVO obj) {
        try {
            CompraDevolucaoVO compra = (CompraDevolucaoVO) obj;
            CallableStatement stm = conn.prepareCall("SP_INSERE_" + tabela);

            stm.setInt("IDFORNECEDOR", compra.getIdFornecedor());
            stm.setDouble("PRECO_VENDA", compra.getPreco());
            stm.setDate("DATA_VENDA", (Date) compra.getData());

            return stm.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Alterar(PadraoVO obj) {
        try {
            CompraDevolucaoVO compra = (CompraDevolucaoVO) obj;
            CallableStatement stm = conn.prepareCall("SP_ATUALIZA_" + tabela);

            stm.setInt("ID", compra.getCodigo());
            stm.setInt("IDFORNECEDOR", compra.getIdFornecedor());
            stm.setDouble("PRECO_VENDA", compra.getPreco());
            stm.setDate("DATA_VENDA", (Date) compra.getData());

            return stm.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Deletar(int id) {
        try {
            CallableStatement stm = conn.prepareCall("SP_EXCLUI_" + tabela);
            stm.setInt("ID", id);
            return stm.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PadraoVO Ler(int id) {
        try {
            CallableStatement stm = conn.prepareCall("SP_CONSULTA_" + tabela);
            stm.setInt("ID", id);
            ResultSet result = stm.executeQuery();

            //TODO: verificar se é compra ou dev e mudar nome dos parametros

            CompraDevolucaoVO compra = new CompraDevolucaoVO();
            compra.setCodigo(result.getInt("ID"));
            compra.setIdFornecedor(result.getInt("IDFORNECEDOR"));
            compra.setPreco(result.getDouble("PRECO_VENDA"));
            compra.setData(result.getDate("DATA_VENDA"));

            return compra;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList<PadraoVO> Listar(boolean flag) {
        ObservableList<PadraoVO> list = FXCollections.observableArrayList();
        try {
            CallableStatement stm = conn.prepareCall("{call SP_LISTA_COMPRA (?, ?)}");
            stm.setInt("IDFORNECEDOR", 0); //TODO: passar IDFORNECEDOR
            stm.setString("SN_DEVOLUCAO", flag ? "S" : "N");
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                ProdutoVO prod = new ProdutoVO();
                prod.setCodigo(result.getInt("IDPRODUTO"));
                prod.setDescricao(result.getString("DESC_PRODUTO"));
                prod.setPreco(result.getDouble("VALOR_PRODUTO"));
                prod.setFornecedor(result.getInt("IDFORNECEDOR"));
                prod.setQtdeEstoque(result.getInt("QUANTIDADE_ESTOQUE"));
                list.add(prod);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}