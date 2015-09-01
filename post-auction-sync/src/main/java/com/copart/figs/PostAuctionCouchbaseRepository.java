package com.copart.figs;

import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.copart.service.domain.Lot;

public interface PostAuctionCouchbaseRepository extends CouchbaseRepository<Lot, String>
{

}
