package entidades;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Registro {

    private String id;
    private LocalDateTime dataAbertura;

    public Registro() {
        this.id = UUID.randomUUID().toString().substring(0, 6);
        this.dataAbertura = LocalDateTime.now();
    }

    public String getId() { 
        return id; 
    }

    public void setId(String id) { 
        this.id = id; 
    }

    public LocalDateTime getDataAbertura() { 
        return dataAbertura; 
    }

    public void setDataAbertura(LocalDateTime dataAbertura) { 
        this.dataAbertura = dataAbertura; 
    }

    public abstract String exibirDetalhes();

    @Override
    public String toString() {
        return exibirDetalhes();
    }
}