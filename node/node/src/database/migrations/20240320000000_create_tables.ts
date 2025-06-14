import { Knex } from 'knex';

export async function up(knex: Knex): Promise<void> {
  // Criar tabela de cursos
  await knex.schema.createTable('cursos', (table) => {
    table.increments('id').primary();
    table.string('nome', 100).notNullable();
  });

  // Criar tabela de usuários
  await knex.schema.createTable('usuarios', (table) => {
    table.increments('id').primary();
    table.string('foto_url', 255);
    table.string('nome', 100).notNullable();
    table.string('email', 100).notNullable().unique();
    table.string('senha_hash', 255);
    table.integer('curso_id').unsigned().references('id').inTable('cursos');
  });

  // Criar tabela de palestrantes
  await knex.schema.createTable('palestrantes', (table) => {
    table.increments('id').primary();
    table.string('nome', 100).notNullable();
    table.text('mini_curriculo');
    table.string('tema', 150);
    table.string('foto_url', 255);
  });

  // Criar tabela de eventos
  await knex.schema.createTable('eventos', (table) => {
    table.increments('id').primary();
    table.string('nome', 150).notNullable();
    table.date('data_inicio');
    table.date('data_fim');
    table.time('hora');
    table.string('endereco', 150).notNullable();
    table.string('foto_url', 255);
    table.integer('curso_id').unsigned().references('id').inTable('cursos');
    table.integer('palestrante_id').unsigned().references('id').inTable('palestrantes');
  });

  // Criar tabela de inscrições
  await knex.schema.createTable('inscricoes', (table) => {
    table.increments('id').primary();
    table.integer('usuario_id').unsigned().notNullable().references('id').inTable('usuarios');
    table.integer('evento_id').unsigned().notNullable().references('id').inTable('eventos');
    table.timestamp('data_inscricao').defaultTo(knex.fn.now());
    table.boolean('aprovado').defaultTo(false);
    table.unique(['usuario_id', 'evento_id']);
  });
}

export async function down(knex: Knex): Promise<void> {
  await knex.schema.dropTable('inscricoes');
  await knex.schema.dropTable('eventos');
  await knex.schema.dropTable('palestrantes');
  await knex.schema.dropTable('usuarios');
  await knex.schema.dropTable('cursos');
} 