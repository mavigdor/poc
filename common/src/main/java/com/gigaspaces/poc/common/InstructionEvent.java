package com.gigaspaces.poc.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * Created by moran on 12/28/15.
 */
@SpaceClass
public class InstructionEvent {

    private String id;
    private String instructionId;
    private String journeyId;

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
     * @return the instruction id
     */
    public String getInstructionId() {
        return instructionId;
    }

    /**
     * @param instructionId the instruction id
     */
    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    /**
     * @param journeyId the journey id of this instruction
     */
    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    /**
     * collocated with instruction
     * @return the journey id
     */
    @SpaceRouting
    public String getJourneyId() {
        return journeyId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
                "id='" + id + '\'' +
                ", instructionId='" + instructionId + '\'' +
                ", journeyId='" + journeyId + '\'' +
                '}';
    }
}
