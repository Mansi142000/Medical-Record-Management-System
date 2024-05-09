package src;

import java.sql.SQLException;

/**
 * The main class for the Medical Records System (MRS) application.
 * This class is responsible for starting the application by creating an instance of the
 * {@link OpeningFrame} and setting it as visible.
 */
public class MRSMain {
  /**
   * The main entry point for the Medical Records System (MRS) application.
   *
   * @param args Command-line arguments (not used in this application).
   * @throws SQLException If there is an error related to database connections or operations.
   */
  public static void main(String[] args) throws SQLException {
    new OpeningFrame().setVisible(true);
  }
}
