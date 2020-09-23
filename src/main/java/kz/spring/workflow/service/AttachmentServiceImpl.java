package kz.spring.workflow.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.spring.workflow.service.DAL.AttachmentService;
import kz.spring.workflow.service.util.Attachment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations operations;

    @Autowired
    public AttachmentServiceImpl(GridFsTemplate gridFsTemplate, GridFsOperations operations) {
        this.gridFsTemplate = gridFsTemplate;
        this.operations = operations;
    }


    @Override
    public String addAttachment(String name, String type, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", type);
        metaData.put("name", name);
        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), name, file.getContentType(), metaData);
        return id.toString();
    }

    @Override
    public void deleteAttachment(String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
    }

    @Override
    public Attachment getAttachment(String id) throws IllegalStateException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        Attachment attachment = new Attachment();
        attachment.setName(file.getMetadata().get("name").toString());
        attachment.setType(file.getMetadata().get("type").toString());
        attachment.setStream(operations.getResource(file).getInputStream());
        return attachment;
    }
}
