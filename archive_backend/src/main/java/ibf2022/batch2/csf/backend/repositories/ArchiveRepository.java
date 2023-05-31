package ibf2022.batch2.csf.backend.repositories;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.models.Bundle;

import static ibf2022.batch2.csf.backend.util.Util.*;

@Repository
public class ArchiveRepository {

	@Autowired
	private MongoTemplate template;

	private static final String COLLECTION_ARCHIVES = "archives";

	// TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	// 
	// db.archives.insertOne({
	// 	bundleId: '4e73d23d',
	// 	date: '2023-05-31',
	// 	title: 'sample title',
	// 	name: 'random name',
	// 	comments: 'a long story that SEOs so excellently it gets ranked #1 on all search engines',
	// 	urls: [
	// 		'https://csfb2.legendory.com/images/9ff64949',
	// 		'https://csfb2.legendory.com/images/5dd9bc7d',
	// 		'https://csfb2.legendory.com/images/0898fdeb',
	// 		'https://csfb2.legendory.com/images/6e57baf4',
	// 		'https://csfb2.legendory.com/images/ea96b660',
	// 		'https://csfb2.legendory.com/images/4a92ffd2',
	// 		'https://csfb2.legendory.com/images/b9dccde0',
	// 		'https://csfb2.legendory.com/images/c47104de'
	// 	]
	// });
	//
	public String recordBundle(String name, String title, String comments, List<String> urls) {
		String bundleId = generateUUID();

		Bundle b = new Bundle(
				bundleId,
				LocalDate.now(),
				title,
				name,
				comments,
				urls);

		Document d = Document.parse(b.toJson().toString());
		template.insert(d, COLLECTION_ARCHIVES);

		return bundleId;
	}

	// TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	// 
	// db.archives.findOne({
	//  	bundleId: '0092cc6f'
	// 	}, 
	// 	{
	// 		_id: 0
	// 	}
	// )
	//
	public Optional<Bundle> getBundleByBundleId(String bundleId) {
		Query q = new Query()
				.addCriteria(Criteria.where("bundleId").is(bundleId));

		q.fields().exclude("_id");

		Document result = template.findOne(q, Document.class, COLLECTION_ARCHIVES);
		if(result == null) {
			return Optional.empty();
		}

		return Optional.of(Bundle.create(result.toJson()));
	}

	// TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	// 
	//  db.archives.aggregate([ { $project: { bundleId: 1, date: 1, title: 1 } }, { $sort: { date: -1, title: 1 } }])
	//
	public Optional<List<Bundle>> getBundles() {
		ProjectionOperation pOp = Aggregation.project("bundleId","date","title");
		SortOperation sOp = Aggregation.sort(Direction.DESC, "date").and(Direction.ASC,"title");

		List<Document> docResults = template.aggregate(Aggregation.newAggregation(pOp, sOp), COLLECTION_ARCHIVES, Document.class).getMappedResults();
		
		if(docResults.size() == 0 ) {
			return Optional.empty();
		}

		List<Bundle> results = new LinkedList<>();
		docResults.forEach(d -> results.add(Bundle.create(d.toJson())));
		return Optional.of(results);
	}

}
