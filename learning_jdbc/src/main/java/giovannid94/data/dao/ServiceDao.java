package giovannid94.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import giovannid94.data.entity.Service;
import giovannid94.data.util.DatabaseUtils;

public class ServiceDao implements Dao<Service, UUID> {
  private final static Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());
  
  private static final String GET_ALL = "select service_id, name, price from wisdom.services";
  private static final String GET_BY_ID = "select service_id, name, price from wisdom.services where service_id = ?";
  private static final String CREATE = "insert into wisdom.services (service_id, name, price) values (?,?,?)";
  private static final String UPDATE = "update wisdom.services set name = ?, price = ? where service_id = ?";
  private static final String DELETE = "delete from wisdom.services where service_id = ?";
  private static final String GET_ALL_LIMIT = "select service_id, name, price from wisdom.services order by name limit = ?"; 

  @Override
  public Optional<Service> getOne(UUID id) {
    try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      List<Service> services = this.processResultSet(rs);
      if (services.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(services.get(0));
    } catch (SQLException e) {
      DatabaseUtils.handleSQLException("ServiceDAO.getOne", e, LOGGER);
    }
    return Optional.empty();
  }

  @Override
  public List<Service> getAll() {
    List<Service> services = new ArrayList<>();
    Connection connection = DatabaseUtils.getConnection();
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(GET_ALL);
      services = this.processResultSet(rs);
    } catch (SQLException e) {
      DatabaseUtils.handleSQLException("ServiceDAO.getAll", e, LOGGER);
    }

    return services;
  }

  public List<Service> getAllLimit(int limit) {
    List<Service> services = new ArrayList<>();
    Connection connection = DatabaseUtils.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_LIMIT)) {
      statement.setInt(1, limit);
      ResultSet rs = statement.executeQuery();
      services = this.processResultSet(rs);
      rs.close();
    } catch (SQLException e) {
      DatabaseUtils.handleSQLException("ServiceDao.getAll", e, LOGGER);
    }
    return services;
  }

  @Override
  public Service create(Service entity) {
    UUID serviceId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(CREATE);    ) {
      connection.setAutoCommit(false);
      statement.setObject(1, serviceId);
      statement.setString(2, entity.getName());
      statement.setBigDecimal(3, entity.getPrice());
      statement.execute();
      connection.commit();
      entity.setServiceId(serviceId);
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSQLException("ServiceDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSQLException("ServiceDao.create", e, LOGGER);
    }
    Optional<Service> service = this.getOne(serviceId);
    if (!service.isPresent()) {
      //qui sarebbe bene gestire il null in qualche maniera con un try/catch block ma per il momento lo lasciamo così
      //sarà da implementare dopo, è bene evitare di mettere null come return statement
      return null;
    }
    return service.get();
  }

  @Override
  public Service update(Service entity) {
    Connection connection = DatabaseUtils.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(UPDATE);) {
      connection.setAutoCommit(false);
      statement.setString(1, entity.getName());
      statement.setBigDecimal(2, entity.getPrice());
      statement.setObject(3, entity.getServiceId());
      statement.execute();
      connection.commit();
      Optional<Service> existingService = this.getOne(entity.getServiceId());
      if(!existingService.isPresent()){
        LOGGER.warning("Cannot update non-existing service with ID: " + entity.getServiceId());
        return null;
      }
      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        LOGGER.warning("Update statement did not affect any rows for ID: " + entity.getServiceId());
        // Il record con questo ID non esiste oppure i dati sono identici a quelli già presenti
        }
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSQLException("ServiceDao.update.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSQLException("ServiceDao.update", e, LOGGER);
    }
    return this.getOne(entity.getServiceId()).get();
  }

  @Override
  public void delete(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
      connection.setAutoCommit(false);
      statement.setObject(1, id);
      statement.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSQLException("ServiceDao.delete.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSQLException("ServiceDao.delete", e, LOGGER);
    }
  }

  private List<Service> processResultSet(ResultSet rs) throws SQLException {
    List<Service> services = new ArrayList<>();
    while (rs.next()) {
      Service service = new Service();
      service.setServiceId((UUID)rs.getObject("service_id"));
      service.setName(rs.getString("name"));
      service.setPrice(rs.getBigDecimal("price"));
      services.add(service);
    }

    return services;
  }

}
