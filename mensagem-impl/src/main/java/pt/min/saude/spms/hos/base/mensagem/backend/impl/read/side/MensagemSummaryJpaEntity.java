package pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MensagemSummaryJpaEntity {
    private String id;

    @NotNull private String json;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
