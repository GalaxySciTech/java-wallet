-- Optional migration: per-chain signing backend (TOKENCORE | EXTERNAL)
ALTER TABLE `wallet_chain_config`
  ADD COLUMN `signing_backend` varchar(32) NOT NULL DEFAULT 'TOKENCORE'
  COMMENT 'TOKENCORE=in-process tokencore; EXTERNAL=wallet.external-signer HTTP'
  AFTER `withdraw_enabled`;
