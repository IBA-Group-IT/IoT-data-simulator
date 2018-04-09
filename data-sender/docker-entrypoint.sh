#!/bin/bash
set -eux

if [[ -v DATA_SENDER_DEBUG_MODE ]] && [ "$DATA_SENDER_DEBUG_MODE" = true ]; then

	echo "Starting app in remote debug mode..."
    exec npm run debug

else
	exec npm run server
fi
