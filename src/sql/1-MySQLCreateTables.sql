-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------
DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

SET FOREIGN_KEY_CHECKS=0; -- to disable them
DROP TABLE IF EXISTS Card;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Bid;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS UserProfile;
SET FOREIGN_KEY_CHECKS=1; -- to re-enable them


-- ------------------------------ UserProfile ----------------------------------
CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL,
    email VARCHAR(60) NOT NULL,
    card BIGINT,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName))
    -- La restriccion de la tarjeta se pone mas abajo 
    ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

-- ------------------------------ Category ----------------------------------
CREATE TABLE Category (
    categoryId BIGINT NOT NULL AUTO_INCREMENT,
    categoryName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    CONSTRAINT Category_PK PRIMARY KEY (categoryId),
    CONSTRAINT CategroyNameUniqueKey UNIQUE (categoryName)) 
    ENGINE = InnoDB;

CREATE INDEX CategoryIndexByCategoryName ON Category (categoryName);

-- ------------------------------ Product ----------------------------------
CREATE TABLE Product (
    productId BIGINT NOT NULL AUTO_INCREMENT,
    productName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    category BIGINT NOT NULL,
    owner BIGINT NOT NULL,
    description VARCHAR(100),
    initDate DATETIME NOT NULL,
    endDate DATETIME NOT NULL,
    initPrice FLOAT(10,2) NOT NULL,
    shipmentDescription VARCHAR(100),
    winBid BIGINT,
    currentPrice FLOAT(10,2),
    version BIGINT,
    CONSTRAINT Product_PK PRIMARY KEY (productId),
    CONSTRAINT ProductCategory_FK FOREIGN KEY (category) REFERENCES Category(categoryId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT ProductOwner_FK FOREIGN KEY (owner) REFERENCES UserProfile(usrId)) 
   	-- La restriccion de la puja que va ganando se pone mas abajo
    ENGINE = InnoDB;

CREATE INDEX ProductIndexByProductName ON Product (ProductName);
CREATE INDEX ProductIndexByWinBid ON Product (winBid);

-- ------------------------------ Bid ----------------------------------
CREATE TABLE Bid (
    bidId BIGINT NOT NULL AUTO_INCREMENT,
    bidDate DATETIME NOT NULL,
    maxPrice FLOAT(10,2) NOT NULL,
    product BIGINT NOT NULL,
    owner BIGINT NOT NULL,
    CONSTRAINT Bid_PK PRIMARY KEY (bidId),
    CONSTRAINT BidProduct_FK FOREIGN KEY (product) REFERENCES Product(productId),
    CONSTRAINT BidOwner_FK FOREIGN KEY (owner) REFERENCES  UserProfile(usrId)
    )
    ENGINE = InnoDB;

CREATE INDEX BidIndexByProduct ON Bid (product);
-- CREATE UNIQUE INDEX BidIndexByProductUser ON Bid (owner, product);
-- ALTER TABLE Bid ADD CONSTRAINT ProductUser_U_Bid UNIQUE (owner, product);

-- ------------------------------ Card ----------------------------------
CREATE TABLE Card (
    numberId BIGINT NOT NULL,
    expiryDate  DATE NOT NULL,
    CONSTRAINT Card_PK PRIMARY KEY (numberId))
    ENGINE = InnoDB;
--Se entiende que no se realizaran busquedas de tarjetas

ALTER TABLE UserProfile ADD CONSTRAINT UserCard_FK FOREIGN KEY (card) REFERENCES Card(numberId) ON DELETE  CASCADE ON UPDATE CASCADE;
ALTER TABLE Product ADD CONSTRAINT ProductBid_FK FOREIGN KEY (winBid) REFERENCES Bid(bidId) ON DELETE CASCADE ON UPDATE CASCADE;
