/*
 *
 *  * COPYRIGHT NOTICE
 *  *
 *  * © Finmeadows, 2024-25. All rights reserved.
 *  *
 *  * This software is the confidential and proprietary information of Finmeadows
 *  * ("Confidential Information"). You shall not disclose such Confidential
 *  * Information and shall use it only in accordance with the terms of the license
 *  * agreement you entered into with Finmeadows.
 *  *
 *  * Finmeadows MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 *  * NON-INFRINGEMENT. Finmeadows SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 *  * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
 *  * ITS DERIVATIVES.
 *  *
 *  *  PRESIDENT OF TECH: Sager Pawaar
 *
 *  *
 *  * Author: Sager Pawaar
 *
 */

package com.sj.ecommerce.utils;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "sequence_collection")
public class SequenceCollection {

    @Id
    private String id;
    private Map<String, Integer> sequences;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Integer> getSequences() {
        return sequences;
    }

    public void setSequences(Map<String, Integer> sequences) {
        this.sequences = sequences;
    }

    // Getters and setters
}
