--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: arguments; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE arguments (
    id integer NOT NULL,
    text character varying,
    pos character varying,
    arglabel character varying,
    deplabel character varying,
    fact_id integer NOT NULL
);


--
-- Name: facts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE facts (
    id integer NOT NULL,
    source character varying,
    sentence character varying,
    text character varying,
    lemma character varying,
    rolesetid character varying
);


--
-- Name: arguments_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY arguments
    ADD CONSTRAINT arguments_pkey PRIMARY KEY (id, fact_id);


--
-- Name: facts_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY facts
    ADD CONSTRAINT facts_pkey PRIMARY KEY (id);


--
-- Name: arguments_fact_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX arguments_fact_id_idx ON arguments USING btree (fact_id);


--
-- Name: facts_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX facts_id_idx ON facts USING btree (id);


--
-- Name: facts_rolesetID_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX "facts_rolesetID_idx" ON facts USING btree (rolesetid);


--
-- Name: arguments_fact_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY arguments
    ADD CONSTRAINT arguments_fact_id_fkey FOREIGN KEY (fact_id) REFERENCES facts(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

