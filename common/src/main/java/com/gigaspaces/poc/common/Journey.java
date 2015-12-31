package com.gigaspaces.poc.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moran on 12/28/15.
 */
@Entity
@Table(name="JOURNEY")
@SpaceClass
public class Journey implements Serializable {
    @Id
    private String id;
    private JourneyLifecycle journeyLifecycle;
    private List<Instruction> instructions;

    /**
     * The id of this object.
     */
    @SpaceRouting //optional (@SpaceId is used for routing if not annotated)
    @SpaceId(autoGenerate=false)
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
     * @return  The lifecycle stage of a journey
     */
    public JourneyLifecycle getJourneyLifecycle() {
        return journeyLifecycle;
    }

    /**
     * @param journeyLifecycle The lifecycle stage of the journey
     */
    public void setJourneyLifecycle(JourneyLifecycle journeyLifecycle) {
        this.journeyLifecycle = journeyLifecycle;
    }

    /**
     * @return the journey instructions to fulfill
     */
    public List<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * @param instructions instructions to attach to this journey
     */
    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    /**
     * @return The next instruction in this journey or {@code null} otherwise.
     */
    public Instruction getNextInstruction() {
        if (instructions.isEmpty()) {
            return null;
        }
        //copy for transaction safety if rollback
        instructions = new ArrayList<Instruction>(instructions);
        return instructions.remove(0);
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
                "id='" + id + '\'' +
                ", journeyLifecycle=" + journeyLifecycle +
                ", instructions=" + instructions +
                '}';
    }
}
