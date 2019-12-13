package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Repository
public class CustomEventRepositoryImpl implements CustomEventRepository {

    private EntityManager em;

    public CustomEventRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Page<Event> executeCustomQuery(Pageable pageable, String searchQuery, String dateFilter, String typeFilter, String locationFilter) {
        String queryString = "select e from Event e where ";

        boolean andNeeded = false;

        if (searchQuery != null) {
            searchQuery = removeSingleQuotationMarks(searchQuery);
            String queryAddition = "e.name like '%" + searchQuery + "%'";
            queryString += queryAddition;
            andNeeded = true;
        }

        if (dateFilter != null) {
            dateFilter = removeSingleQuotationMarks(dateFilter);
            queryString = addAndIfNeeded(queryString, andNeeded);
            String queryAddition = "e.id in (select ed.event from EventDay ed where ed.date like '" + dateFilter + "')";
            queryString += queryAddition;
            andNeeded = true;
        }

        if (typeFilter != null) {
            typeFilter = removeSingleQuotationMarks(typeFilter);
            queryString = addAndIfNeeded(queryString, andNeeded);
            String queryAddition = "e.category like '" + typeFilter + "'";
            queryString += queryAddition;
            andNeeded = true;
        }

        if(locationFilter != null) {
            locationFilter = removeSingleQuotationMarks(locationFilter);
            queryString = addAndIfNeeded(queryString, andNeeded);
            String queryAddition = "e.id in (select ed.event from EventDay ed where ed.location in " +
                    "(select l.id from Location l where l.scheme in (select ls.id from LocationScheme " +
                    "ls where ls.name like '%" + locationFilter + "%')))";
            queryString += queryAddition;
        }

        TypedQuery<Event> query = em.createQuery(queryString, Event.class)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
    }

    private String addAndIfNeeded(String queryString, boolean andNeeded) {
        if (andNeeded) {
            queryString += " and ";
        }
        return queryString;
    }

    private String removeSingleQuotationMarks(String filterParam) {
        return filterParam.replace("'", "");
    }
}
