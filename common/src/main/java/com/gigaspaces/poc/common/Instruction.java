package com.gigaspaces.poc.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by moran on 12/28/15.
 */
@Entity
@Table(name="INSTRUCTION")
@SpaceClass
public class Instruction implements Serializable {
    @Id
    private String id;
    private String journeyId;
    private InstructionLifecycle instructionLifecycle;

    /**
     * The id of this object.
     */
    @SpaceId(autoGenerate=true)
    public String getId() {
        return id;
    }

    /**
     * The id of this object. Its value will be auto generated when it is written to the space.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * collocate instruction with journey
     * @return the journey id
     */
    @SpaceRouting
    public String getJourneyId() {
        return journeyId;
    }

    /**
     * @param journeyId set the journey id
     */
    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    /**
     * @return the instruction lifecyle stage
     */
    public InstructionLifecycle getInstructionLifecycle() {
        return instructionLifecycle;
    }

    /**
     * @param instructionLifecycle the instruction lifecycle
     */
    public void setInstructionLifecycle(InstructionLifecycle instructionLifecycle) {
        this.instructionLifecycle = instructionLifecycle;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
                "id='" + id + '\'' +
                ", journeyId='" + journeyId + '\'' +
                ", instructionLifecycle=" + instructionLifecycle +
                '}';
    }
}
