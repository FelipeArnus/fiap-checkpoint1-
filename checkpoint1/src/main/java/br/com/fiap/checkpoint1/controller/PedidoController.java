package br.com.fiap.checkpoint1.controller;

import br.com.fiap.checkpoint1.model.Pedido;
import br.com.fiap.checkpoint1.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pedido criar(@RequestBody Pedido pedido) {
        return pedidoService.salvar(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (pedidoService.buscarPorId(id).isPresent()) {
            pedidoService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedidoAtualizado) {
        return pedidoService.buscarPorId(id)
                .map(pedidoExistente -> {
                    pedidoExistente.setClienteNome(pedidoAtualizado.getClienteNome());
                    pedidoExistente.setValorTotal(pedidoAtualizado.getValorTotal());
                    return ResponseEntity.ok(pedidoService.salvar(pedidoExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
