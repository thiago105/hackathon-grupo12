/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = async function(knex) {
  await knex.schema.createTable('cursos', (table) => {
    table.increments('id').unsigned().primary();
    table.string('nome', 100).notNullable();
    table.timestamp('created_at').defaultTo(knex.fn.now());
    table.timestamp('updated_at').defaultTo(knex.fn.now());
  });

  await knex.schema.createTable('palestrantes', (table) => {
    table.increments('id').unsigned().primary();
    table.string('nome', 100).notNullable();
    table.text('mini_curriculo');
    table.string('tema', 250);
    table.string('foto_url', 255);
    table.timestamp('created_at').defaultTo(knex.fn.now());
    table.timestamp('updated_at').defaultTo(knex.fn.now());
  });

  await knex.schema.createTable('eventos', (table) => {
    table.increments('id').unsigned().primary();
    table.string('nome', 150).notNullable();
    table.date('data_inicio');
    table.time('hora');
    table.string('endereco', 150).notNullable();
    table.string('foto_url', 255);
    table.integer('curso_id').unsigned().references('id').inTable('cursos');
    table.integer('palestrante_id').unsigned().references('id').inTable('palestrantes');
    table.timestamp('created_at').defaultTo(knex.fn.now());
    table.timestamp('updated_at').defaultTo(knex.fn.now());
  });

  await knex.schema.createTable('usuarios', (table) => {
    table.increments('id').unsigned().primary();
    table.string('foto_url', 255);
    table.string('nome', 100).notNullable();
    table.string('email', 100).notNullable().unique();
    table.string('senha_hash', 255);
    table.string('confirmarSenha_hash', 255);
    table.integer('curso_id').unsigned().references('id').inTable('cursos');
    table.timestamp('created_at').defaultTo(knex.fn.now());
    table.timestamp('updated_at').defaultTo(knex.fn.now());
  });

  await knex.schema.createTable('inscricoes', (table) => {
    table.increments('id').unsigned().primary();
    table.integer('usuario_id').unsigned().notNullable().references('id').inTable('usuarios').onDelete('CASCADE');
    table.integer('evento_id').unsigned().notNullable().references('id').inTable('eventos').onDelete('CASCADE');
    table.dateTime('data_inscricao').defaultTo(knex.fn.now());
    table.boolean('aprovado').defaultTo(false);
    table.unique(['usuario_id', 'evento_id']);
    table.timestamp('created_at').defaultTo(knex.fn.now());
    table.timestamp('updated_at').defaultTo(knex.fn.now());
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = async function(knex) {
  await knex.schema.dropTableIfExists('inscricoes');
  await knex.schema.dropTableIfExists('usuarios');
  await knex.schema.dropTableIfExists('eventos');
  await knex.schema.dropTableIfExists('palestrantes');
  await knex.schema.dropTableIfExists('cursos');
};