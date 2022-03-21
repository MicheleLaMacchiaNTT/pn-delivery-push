package it.pagopa.pn.deliverypush.middleware.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@DynamoDbBean
public class PaperNotificationFailedEntity {
    public static final String TABLE_NAME = "PaperNotificationFailed";
    public static final String FIELD_RECIPIENT_ID = "recipientId";
    public static final String FIELD_IUN = "iun";

    private String recipientId;
    private String iun;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "recipientId")
    public String getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "iun")
    public String getIun() {
        return iun;
    }
    public void setIun(String iun) {
        this.iun = iun;
    }
    
}

