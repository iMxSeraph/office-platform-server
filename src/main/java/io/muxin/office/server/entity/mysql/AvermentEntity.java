package io.muxin.office.server.entity.mysql;

import io.muxin.office.server.common.constant.AvermentStatus;
import io.muxin.office.server.common.constant.AvermentType;

import javax.persistence.*;

/**
 * 申辩信息实体
 * Created by muxin on 2017/2/26.
 */
@Entity
@Table(name = "averment")
public class AvermentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "summary_id", referencedColumnName = "id")
    private SummaryEntity summary;

    @Enumerated(EnumType.ORDINAL)
    private AvermentType type;

    @Enumerated(EnumType.ORDINAL)
    private AvermentStatus status;

    private String operator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SummaryEntity getSummary() {
        return summary;
    }

    public void setSummary(SummaryEntity summary) {
        this.summary = summary;
    }

    public AvermentType getType() {
        return type;
    }

    public void setType(AvermentType type) {
        this.type = type;
    }

    public AvermentStatus getStatus() {
        return status;
    }

    public void setStatus(AvermentStatus status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
