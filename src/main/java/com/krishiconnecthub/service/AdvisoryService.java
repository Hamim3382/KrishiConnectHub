package com.krishiconnecthub.service;

import com.krishiconnecthub.dto.AdvisoryRequestDTO;
import com.krishiconnecthub.model.AdvisoryRequest;

import java.io.IOException;

public interface AdvisoryService {

    /**
     * Creates an advisory request, gets a suggestion from an external AI service,
     * and saves the request and suggestion to the database.
     * @param requestDTO The advisory request data from the user.
     * @return The saved advisory request containing the generated suggestion.
     * @throws IOException if the API call fails.
     * @throws InterruptedException if the thread is interrupted.
     */
    AdvisoryRequest getAdvisoryAndSave(AdvisoryRequestDTO requestDTO) throws IOException, InterruptedException;

}